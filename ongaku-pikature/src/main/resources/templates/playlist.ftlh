<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<#import "spring.ftl" as spring />
<head>
    <meta charset="UTF-8">
    <title>Playlist</title>
    <base href="/">
    <link href="css/welcome_page.css" rel="stylesheet">
    <link href="css/profile.css" rel="stylesheet">


    <script src="https://code.jquery.com/jquery-2.2.4.js" charset="utf-8"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="https://use.fontawesome.com/releases/v5.0.8/js/all.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <#--    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">-->
</head>

<script>
    /* Установить ширину боковой панели с шириной 250 пикселей и следующий */
    function openNav() {
        document.getElementById("mySidenav").style.width = "310px";
    }

    /* Установите ширину боковой навигации в 0 */
    function closeNav() {
        document.getElementById("mySidenav").style.width = "0";
    }
</script>
<script>
    function getXmlHttp() {
        var xmlhttp;
        try {
            xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (E) {
                xmlhttp = false;
            }
        }
        if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
            xmlhttp = new XMLHttpRequest();
        }
        return xmlhttp
    }
</script>
<script>
    $(document).on('click', '.like', function (event) {

        postId = $(this).attr('data-id');
        flag = music_like[postId];
        var req = getXmlHttp()

        // (2)
        // span рядом с кнопкой
        // в нем будем отображать ход выполнения
        var x = document.getElementById('vote_favourite_status')


        // (3) задать адрес подключения
        req.open('POST', '/song/' + postId + '/like?isLike=' + flag, true);

        // объект запроса подготовлен: указан адрес и создана функция onreadystatechange
        // для обработки ответа сервера

        // (4)
        req.send(null);  // отослать запрос


        req.onreadystatechange = function () {
            // onreadystatechange активируется при получении ответа сервера

            if (req.readyState === 4) {
                // если запрос закончил выполняться


                if (req.status === 200) {
                    // если статус 200 (ОК) - выдать ответ пользователю
                    if (flag) {
                        alert("Песня " + music_name[postId] + " добавлена в избранное");
                        event.target.style.backgroundColor = 'red'
                        flag = false;
                    } else {
                        alert("Песня " + music_name[postId] + " удалена из избранного");
                        event.target.style.backgroundColor = 'white'
                        flag = true;
                    }
                    music_like[postId] = flag;

                } else {
                    alert("Извините, произошла ошибка");
                }
            }
        }

    });
</script>

<script>
    $(document).on('click', '.delete', function (event) {

        musicId = $(this).attr('musicId');
        var req = getXmlHttp()

        // (2)
        // span рядом с кнопкой
        // в нем будем отображать ход выполнения
        var x = document.getElementById('vote_favourite_status')


        // (3) задать адрес подключения
        req.open('DELETE', '/${login}/playlists/${playlistInfo.id()}/' + musicId, true);

        // объект запроса подготовлен: указан адрес и создана функция onreadystatechange
        // для обработки ответа сервера

        // (4)
        req.send(null);  // отослать запрос


        req.onreadystatechange = function () {
            // onreadystatechange активируется при получении ответа сервера

            if (req.readyState === 4) {
                // если запрос закончил выполняться


                if (req.status === 200) {
                    // если статус 200 (ОК) - выдать ответ пользователю
                    alert("Песня " + music_name[musicId] + " удалена из избранного");
                    event.target.disabled= true

                } else {
                    alert("Извините, произошла ошибка");
                }
            }
        }

    });
</script>

<body>
<button onclick="openNav()">
    <#--    <img class="profile_menu" src="pokeball.png"/>-->
    Profile
</button>

<div id="mySidenav" class="sidenav">
    <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>

    <div class="welcome-page">


        <div class="menu">
            <p class="app-name">OngakuPikature</p>
            <img src="defaultProfileImage.jpg" alt="аватарка" class="user-pic">
            <#if user??>
                <p class="user-name">${user.getUser().login}</p>

                <a class="buttons" href="/${user.getUser().login}/playlists">My playlists</a>
                <a class="buttons" href='/logout'>Logout</a>
            <#else >
                <p class="user-name">Anonimous</p>
                <a class="buttons" href='/signIn'>Sign in</a>
                <a class="buttons" href='/signUp'>Sign Up</a>
            </#if>
            <hr>


        </div>


    </div>

</div>

Playlist: ${playlistInfo.name()}

${user.getUser().login}

<script>
    let music_like = {};
    let music_name = {};
    <#list favouriteMusicIds as favoriteMusicId>
    music_like[${favoriteMusicId}] = false
    </#list>
</script>

<#list playlistMusic as music>
    <script>
        if (!(${music.id()} in music_like)) {
            music_like[${music.id()}] = true
        }
        music_name[${music.id()}] = '${music.name()}'
    </script>

    <br>
    <div>${music.name()}</div>
    <audio controls style="width:100%;max-width:600px;">
        <source src="${music.musicPath()}" type="audio/mp3">
    </audio>
<#if playlistInfo.name() == 'Избранное'>

    <script>
        var color = 'red"'
        if (music_like[${music.id()}] == true) {
            color = 'white"'
        }
        var style = 'style="background-color: ' + color
        // document.write(style)
        document.write('<button class="like"' + style + ' data-id=' + ${music.id()} + '>like</button>')
    </script>
<#else>
    <button class="delete" musicId=${music.id()}>delete</button>
</#if>
</#list>

<#--<div class="main-part">-->
<#--    <div class="header">-->
<#--        <div class="search-part">-->
<#--            <img alt="" class="serach-img" src="https://static.overlay-tech.com/assets/10d973cf-84de-4dd1-9a20-967acd9af517.svg"/>-->
<#--            <p class="search-text">Buscar</p>-->
<#--        </div>-->
<#--    </div>-->

<#--    <div class="playlist">-->
<#--        <div class="playlist-head">-->
<#--            <img alt="" class="prev-playlist" src="prev_playlist.png"/>-->
<#--            <div class="playlist-info">-->
<#--                <p class="playlist-title">Мне нравится</p>-->
<#--                <p class="songs-count">20 треков</p>-->
<#--            </div>-->
<#--            <img alt="" class="play" src="play.png"/>-->
<#--        </div>-->
<#--        <div class="songs">-->
<#--            <div class="songs_colum">-->
<#--                <div class="muz1">-->
<#--                    <div class="muz-prev"></div>-->
<#--                    <div class="info-muz">-->
<#--                        <p class="song-title">Dynamite</p>-->
<#--                        <p class="author1">-->
<#--                            BTS-->
<#--                        </p>-->
<#--                    </div>-->
<#--                </div>-->
<#--                <div class="muz1">-->
<#--                    <div class="muz-prev"></div>-->
<#--                    <div class="info-muz">-->
<#--                        <p class="song-title">Butter</p>-->
<#--                        <p class="author1">-->
<#--                            BTS-->
<#--                        </p>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
<#--            <div class="songs_colum">-->
<#--                <div class="muz1">-->
<#--                    <div class="muz-prev"></div>-->
<#--                    <div class="info-muz">-->
<#--                        <p class="song-title">FAKE LOVE</p>-->
<#--                        <p class="author1">-->
<#--                            BTS-->
<#--                        </p>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->
<#--</div>-->

<script src=" https:
//cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"
        integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13"
        crossorigin="anonymous"></script>
</body>
</html>