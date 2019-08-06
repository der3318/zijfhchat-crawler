
/** background */
document.body.style.background = "url('/images/background.jpg') repeat-y center center";
document.body.style.backgroundSize = "110% 110%";
document.body.style.backgroundAttachment = "fixed";

/** navigation bar */
if (window.location.href.includes("/images")) {
    document.getElementById("images").classList.add("active");
} else if (window.location.href.includes("/zijfhchat")) {
    document.getElementById("zijfhchat").classList.add("active");
} else if (window.location.href.includes("/zijfhclub")) {
    document.getElementById("zijfhclub").classList.add("active");
} else {
    document.getElementById("home").classList.add("active");
}

/** search */
document.getElementById("search").addEventListener("click", function() {
    window.location = "/images?search=" + document.getElementById("input").value;
});

/** footer */
document.getElementById("footer").style.opacity = 0.5;
