
$(window).scroll(function(){
    console.log("scrolled test")
    $('nav').toggleClass('scrolled', $(this).scrollTop() > 50);
});



/* 원주 작업 */
window.onscroll = function() {myFunction()};

function myFunction() {
  var winScroll = document.body.scrollTop || document.documentElement.scrollTop;
  var height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
  var scrolled = (winScroll / height) * 100;
  document.getElementById("myBar").style.width = scrolled + "%";
}

$(function() {
    $("#fs").click(function(e) {
        $("#ss").text("○");
        $("#ts").text("○");
        $(this).text("●");
        var posY = $($(this).attr("href")).position();
        $("html").animate({'scrollTop':posY.top -100}, 500);
        $("html,body").stop().animate({'scrollTop':posY.top -100}, 700);
        e.preventDefault();
    });
    $("#ss").click(function(e) {
        $("#fs").text("○");
        $("#ts").text("○");
        $(this).text("●");
        var posY = $($(this).attr("href")).position();
        $("html").animate({'scrollTop':posY.top -170}, 500);
        $("html,body").stop().animate({'scrollTop':posY.top -170}, 700);
        e.preventDefault();
    });
    $("#ts").click(function(e) {
        $("#ss").text("○");
        $("#fs").text("○");
        $(this).text("●");
        var posY = $($(this).attr("href")).position();
        $("html").animate({'scrollTop':posY.top -10}, 500);
        $("html,body").stop().animate({'scrollTop':posY.top -10}, 700);
        e.preventDefault();
    });
});