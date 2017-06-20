$(document).ready(function(){
    $(".content").load("/myAccountPage.html");
});

function showPage(i) {
        console.log("changing");
        $(".content").load("/myAccountPage.html");
        //$("#content").load("index.html");
    }