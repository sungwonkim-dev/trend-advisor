
// 스크롤 시 메뉴가 투명해지는 것
$(window).scroll(function () {
    $('nav').toggleClass('scrolled', $(this).scrollTop() > 50);
});

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
  });




/* 원주 작업 */
window.onscroll = function () { myFunction() };

function myFunction() {
    var winScroll = document.body.scrollTop || document.documentElement.scrollTop;
    var height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
    var scrolled = (winScroll / height) * 100;
    document.getElementById("myBar").style.width = scrolled + "%";
}
