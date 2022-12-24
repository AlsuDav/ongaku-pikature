<!DOCTYPE html>
<html lang="en">
<#import "spring.ftl" as spring />
<head>
    <meta charset="UTF-8">
    <title>Songs</title>
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
    $(document).on('click', '.music-generate', function () {
        musicId = parseInt($(this).attr('music_id'));
        var req = getXmlHttp()

        // (2)
        // span рядом с кнопкой
        // в нем будем отображать ход выполнения
        var x = document.getElementById('vote_favourite_status')

        // var params = {
        //     "id": musicId
        // }
        // (3) задать адрес подключения
        req.open('POST', '/neuro_image/generate/' + musicId, true);
        // req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        // объект запроса подготовлен: указан адрес и создана функция onreadystatechange
        // для обработки ответа сервера

        // (4)


        req.send(null);  // отослать запрос
        alert("генерация картинки запущена")

        req.onreadystatechange = function () {
            // onreadystatechange активируется при получении ответа сервера
            location.reload();

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
                <p class="user-name">${user.login}</p>

                <a class="buttons" href="/${user.login}/playlists">My playlists</a>
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

${user.login}

<#if musicAddedData.isLiked() >
    Like
<#else >
    Not Like
</#if>
<#if musicAddedData.neuroPicturePath()??>
    <img src="/${musicAddedData.neuroPicturePath()}" alt="poster" width="150px">

<#else >
    <img src="/defaultSongPoster.png" alt="poster" class="user-pic" width="150px">
</#if>

<br>
<#if musicMainData??>
<div>${musicMainData.name()}</div>
<audio controls style="width:100%;max-width:600px;">
    <source src="${musicMainData.musicPath()}" type="audio/mp3">
</audio>

<#if user??>
<#--    <form action="/neuro_image/generate" method="post"-->
<#--          class="form-horizontal">-->
<#--        <input type="hidden" name="id" value="${musicMainData.id()}">-->
<#--        <button type="submit" class="btn btn-outline-danger btn-sm" value="Generate image">Generate Image</button>-->
<#--    </form>-->
    <button class="music-generate" music_id="${musicMainData.id()}">Generate image</button>


<#--    add comment-->

</#if>
</#if>
</body>
</html>