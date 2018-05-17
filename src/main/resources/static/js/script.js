$(document).ready(function() {
    let pageCount = 1;
    let allTweetsLoaded = false;
    let requestDone = new Array();
    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
            if(requestDone[pageCount] == undefined && !allTweetsLoaded) {
                requestDone[pageCount] = true;
                $.get(window.location.pathname+"/"+pageCount, function (data) {
                    pageCount += 1;
                    if(data.length > 0) {
                        $.each(data, function (key, value) {
                            var obj = JSON.parse(value);
                            console.log(obj);
                            $("#tweetwall").append("<div class=\"card col-md-10 mt-5 offset-md-1\">\n" +
                                "                    <div class=\"card-header\">\n" +
                                "                        Bitt\n" +
                                "                        <span class=\"right small\">" + obj.timestamp + "</span>\n" +
                                "                    </div>\n" +
                                "                    <div class=\"card-body\">\n" +
                                "                        <h5 class=\"card-title\">" + obj.text + "</h5>\n" +
                                "                    </div>\n" +
                                "                    <div class=\"card-footer\">\n" +
                                "                        <a href=\"#\" class=\"btn btn-success\">+ " + obj.author + "</a>\n" +
                                "                    </div>\n" +
                                "                </div>");
                        });
                    }
                    else {
                        allTweetsLoaded = true;
                        $("#tweetwall").append("<div class=\"card col-md-12 mt-5 offset-md-12\"><span class='text-center'>Keine Tweets mehr verfügbar</span></div>");
                    }
                });
            }
        }
    });
});