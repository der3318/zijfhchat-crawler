
/** background */
document.getElementById("banner").style.background = "url('/images/board.jpg') repeat-y center center";
document.getElementById("banner").style.backgroundSize = "cover";
document.getElementById("banner").style.opacity = 0.5;
document.getElementById("board").style.opacity = 0.8;
document.getElementById("reload").style.opacity = 0.8;

/* auto reload */
$('.ui.checkbox').checkbox();
setInterval(function() {
    if ($('.ui.toggle').find('input').is(':checked')) {
        window.location = "/zijfhchat/47?reload=true";
    }
},10000);
