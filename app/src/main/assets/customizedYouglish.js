// your_script.js

// This code loads the widget API code asynchronously.
var tag = document.createElement('script');
tag.src = "https://youglish.com/public/emb/widget.js";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// This function creates a widget after the API code downloads.
var widget;
function onYouglishAPIReady(){
  widget = new YG.Widget("widget-1", {
    width: 640,
    components: 72, //search box & caption
    autoStart: 1,
    captionColor: "#044575",
    markerColor: "#044575",
    linkColor: "white",
    events: {
      'onFetchDone': onFetchDone,
      'onVideoChange': onVideoChange,
      'onCaptionConsumed': onCaptionConsumed
    }
  });

  // process the query
  widget.fetch("hello", "english");
}

var views = 0, curTrack = 0, totalTracks = 0;

// The API will call this method when the search is done
function onFetchDone(event){
  if (event.totalResult === 0)
    alert("No result found");
  else
    totalTracks = event.totalResult;
}

// The API will call this method when switching to a new video.
function onVideoChange(event){
  curTrack = event.trackNumber;
  views = 0;
}

// The API will call this method when a caption is consumed.
function onCaptionConsumed(event){
  if (++views < 3)
    widget.replay();
  else
    if (curTrack < totalTracks)
      widget.next();
}

function fetchSearchWord(searchWord) {
  widget.fetch(searchWord, "english");
}

// Rest of your code...

