$(document).ready(function () {
    let pageCount = 1;
    let allTweetsLoaded = false;
    let requestDone = new Array();
    $(window).scroll(function () {
        if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
            if (requestDone[pageCount] === undefined && !allTweetsLoaded) {
                let user = getCookie("client_username");
                if (window.location.pathname.split("/").pop() === "global") {
                    user = "global";
                }
                requestDone[pageCount] = true;
                $.get("/api/messages/" + user + "/" + pageCount, function (data) {
                    pageCount += 1;
                    if (data.messages.length > 0) {
                        $.each(data.messages, function (key, value) {
                            var obj = JSON.parse(value);
                            var currentUser = getCookie("client_username");
                            var followButton;
                            if (currentUser === obj.author) {
                                followButton = "<button class='btn btn-info followButton' style='visibility: HIDDEN'>- " + obj.author + "</button>";
                            } else if (data.subs.includes(obj.author)) {
                                followButton = "<button class='btn btn-info followButton'>- " + obj.author + "</button>";
                            } else {
                                followButton = "<button class='btn btn-success followButton'>+ " + obj.author + "</button>";
                            }

                            $("#tweetwall").append("<div class=\"card col-md-10 mt-5 offset-md-1\">\n" +
                                "                    <div class=\"card-header\">\n" + obj.author +
                                "                        <span class=\"right small\">" + obj.timestamp + "</span>\n" +
                                "                    </div>\n" +
                                "                    <div class=\"card-body\">\n" +
                                "                        <h5 class=\"card-title\">" + obj.text + "</h5>\n" +
                                "                    </div>\n" +
                                "                    <div class=\"card-footer\">\n" +
                                followButton +
                                "                    </div>\n" +
                                "                </div>");
                        });
                        followListener();
                    }
                    else {
                        allTweetsLoaded = true;
                        $("#tweetwall").append("<div class=\"card col-md-12 mt-5 offset-md-12\"><span class='text-center'>Keine Tweets mehr verfügbar</span></div>");
                    }
                });
            }
        }
    });
    $(".post-message").click(function (e) {
        e.preventDefault();
        let author = getCookie("client_username");
        let bitter = {
            "timestamp": null,
            "author": author,
            "text": $('.modal-input').val()
        };
        $('#exampleModal').modal('hide');
        $.ajax("/api/messages/" + author,
            {
                "data": JSON.stringify(bitter),
                "method": "POST",
                "success": function (data, _) {
                    $('.modal-input').val("");
                    location.reload();

                },
                "error": function (_, status, error) {
                    console.log(error)
                },
                "contentType": "application/json"
            });
    });

    function followListener() {
        $(".followButton").click(function (e) {
            e.preventDefault();
            var button = $(this);
            let buttonText = $(this).text();
            let extract = buttonText.lastIndexOf(' ');
            let userToFollow = buttonText.substring(extract + 1);
            let apiPath = buttonText.substring(0, 1) === '+' ? 'follow' : 'unfollow';
            let user = getCookie("client_username");
            let targetUrl = "/api/" + apiPath + "/" + user;
            $.ajax(targetUrl, {
                "data": JSON.stringify({follow: userToFollow}),
                "method": "POST",
                "success": function (data) {
                    if (apiPath === "follow") {
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
                "error": function (_, status, error) {
                    console.log(error);
                },
                "contentType": "application/json"
            });
        });
    }

    followListener();
    $(".userSearchInput").keypress(function (e) {
        let userString = $(this).val();
        if (e.keyCode == 13) { // Enter press
            window.location.href = "/userlist/search/" + userString + "/0";
        }
        else {
            let dataList = $("#searchresults");
            if (userString.length > 0) {
                $.get("/api/usersearch/" + userString, function (data) {
                    var obj = JSON.parse(data);
                    dataList.empty();
                    for (var i = 0, len = obj.length; i < len; i++) {
                        var opt = $("<option></option>").attr("value", obj[i]);
                        dataList.append(opt);
                    }
                });
            }
        }
    });
    $(".toggleLogout").click(function (e) {
        e.preventDefault();
        disconnect();
        $.get("/api/logout", function (data) {
            if (data) {
                window.location.href = "/";
            }
        });
    });
    $(".userSearchButton").click(function (e) {
        e.preventDefault();
        let userLookup = $(".userSearchInput").val();
        window.location.href = "/userlist/search/" + userLookup + "/0";
    });

    $('.modal').on('shown.bs.modal', function () {
        $(this).find('[autofocus]').focus();
    });

    //Connect sockets
    connect();
});

function getCookie(name) {
    let value = "; " + document.cookie;
    let parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}


/* Notifications */
function showNotification(message) {
    alertify.success(message);
}

var stompClient = null;

function connect() {
    let apiPath = "subscriptions"
    let user = getCookie("client_username");
    let targetUrl = "/api/" + apiPath + "/" + user;
    let subs;
    $.ajax(targetUrl, {
        "method": "GET",
        "success": function (data) {
            subs = JSON.parse(data);
            console.log(typeof subs);
        },
        "error": function (_, status, error) {
            console.log(error);
        }
    });

    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        subs.forEach((sub) => stompClient.subscribe('/topic/' + sub, handleChannelMessage));
    });
}

function handleChannelMessage(message) {
    let msgObject = JSON.parse(message.body);
    showNotification("New Bitt:\n" + msgObject.text);
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}