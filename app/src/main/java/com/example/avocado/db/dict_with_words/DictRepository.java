package com.example.avocado.db.dict_with_words;

import androidx.room.Query;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DictRepository {
    private DictDao dictDao;
    private WordDao wordDao;
    public DictRepository(DictDao dictDao,WordDao wordDao) {
        this.wordDao=wordDao;
        this.dictDao = dictDao;
    }

    public Completable insertDict(Dict d) {
        return dictDao.insert(d)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
//    public Completable delete(int dictId)
//    {
//        return dictDao.getDictWithWordsById(dictId)
//                .flatMapCompletable(dictWithWords -> {
//                    List<Word> words = dictWithWords.words;
//                    // Delete the dict and words in a transaction
//                    return Completable.fromAction(() -> {
//                        dictDao.delete(dictWithWords.dict);
//                        wordDao.delete());
//                    });
//                });
//    }



    public Single<List<Dict>> getDictsByModified()
    {
        return dictDao.loadOrderByModified()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Dict> getDictByTitle(String title)
    {
        return dictDao.getDictByTitle(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<DictWithWords> getWordsByDictId(int dictId)
    {
        return wordDao.dictHasWord(dictId)
                .flatMap(hasWord -> {
                    if (hasWord) {
                        return dictDao.getDictWithWordsById(dictId);
                    } else {
                        // Return an empty Flowable if dictId has no associated words
                        return Single.error(new Throwable("해당하는 단어가 없습니다"));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateModifiedTime(int dictId,Date date)
    {
        return dictDao.updateModifiedTime(dictId,date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
