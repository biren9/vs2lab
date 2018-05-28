$(document).ready(function() {
    let pageCount = 1;
    let allTweetsLoaded = false;
    let requestDone = new Array();
    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
            if(requestDone[pageCount] === undefined && !allTweetsLoaded) {
                let user = getCookie("client_username");
                if (window.location.pathname.split("/").pop() === "global") {
                    user = "global";
                }
                requestDone[pageCount] = true;
                $.get("/api/messages/" + user + "/" +pageCount, function (data) {
                    pageCount += 1;
                    if(data.messages.length > 0) {
                        $.each(data.messages, function (key, value) {
                            var obj = JSON.parse(value);
                            console.log(obj);
                            if (data.subs.includes(obj.author)) {
                                $("#tweetwall").append("<div class=\"card col-md-10 mt-5 offset-md-1\">\n" +
                                    "                    <div class=\"card-header\">\n" +
                                    "                        Bitt\n" +
                                    "                        <span class=\"right small\">" + obj.timestamp + "</span>\n" +
                                    "                    </div>\n" +
                                    "                    <div class=\"card-body\">\n" +
                                    "                        <h5 class=\"card-title\">" + obj.text + "</h5>\n" +
                                    "                    </div>\n" +
                                    "                    <div class=\"card-footer\">\n" +
                                    "                        <a href=\"#\" class=\"btn btn-info\">- " + obj.author + "</a>\n" +
                                    "                    </div>\n" +
                                    "                </div>");
                            } else {
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
                            }

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
    $(".post-message").click(function(e) {
        e.preventDefault();
        let author = getCookie("client_username");
        let bitter = {
            "timestamp" : null,
            "author" : author,
            "text" : $('.modal-input').val()
        };
        $('#exampleModal').modal('hide');
        $.ajax("/api/messages/" + author,
            {
                "data" : JSON.stringify(bitter),
                "method" : "POST",
                "success" : function(data, _) {
                    $('.modal-input').val("");
                    location.reload();

                },
                "error": function(_, status, error ) {
                    console.log(error)
                },
                "contentType" : "application/json"
            });
    });
    $(".followButton").click(function(e) {
        e.preventDefault();
        var button = $(this);
        let buttonText = $(this).text();
        let extract = buttonText.lastIndexOf(' ');
        let userToFollow = buttonText.substring(extract + 1);
        let apiPath = buttonText.substring(0, 1) === '+'? 'follow' : 'unfollow';
        let user = getCookie("client_username");
        let targetUrl = "/api/"+apiPath+"/" + user;
        $.ajax(targetUrl, {
                "data" : JSON.stringify({follow: userToFollow}),
                "method" : "POST",
                "success" : function(data) {
                    console.log(apiPath);
                    if(apiPath === "follow") {
                        button.removeClass("btn-success");
                        button.addClass("btn-info");
                        button.text("- " + userToFollow);
                    }
                    else {
                        button.removeClass("btn-info");
                        button.addClass("btn-success");
                        button.text("+ " + userToFollow);
                    }
                },
                "error": function(_, status, error ) {
                    console.log(error);
                },
                "contentType" : "application/json"
        });
    });
});

function getCookie(name) {
    let value = "; " + document.cookie;
    let parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}