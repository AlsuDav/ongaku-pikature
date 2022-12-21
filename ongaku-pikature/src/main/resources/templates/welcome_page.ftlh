<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<#import "spring.ftl" as spring />
<head>
    <meta charset="UTF-8">
    <title>Welcome Page</title>
    <base href="/">
    <link href="css/welcome_page.css" rel="stylesheet">
    <link href="css/profile.css" rel="stylesheet">

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
    <script src="https://code.jquery.com/jquery-2.2.4.js" charset="utf-8"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<#--    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">-->
</head>
<body>

<div id="mySidenav" class="sidenav">
    <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>

    <div class="welcome-page">


        <div class="menu">
            <p class="app-name">OngakuPikature</p>
            <img src="defaultProfileImage.jpg" alt="аватарка" class="user-pic">
            <#if user??>
                <p class="user-name">${user.getUser().login}</p>

                <a class="buttons" href="/${user.getUser().login}/playlists" >My playlists</a>
                <a class="buttons" href='/logout' >Logout</a>
            <#else >
                <p class="user-name">Anonimous</p>
                <a class="buttons" href='/signIn' >Sign in</a>
                <a class="buttons" href='/signUp' >Sign Up</a>
            </#if>
            <hr>



        </div>


<#--        <div class="main-part">-->
<#--            <div class="header">-->
<#--                <img-->
<#--                        alt=""-->
<#--                        class="conect"-->
<#--                        src="https://static.overlay-tech.com/assets/5ece3fe1-615a-49ed-8fde-c7559ecc92c3.svg"-->
<#--                />-->
<#--                <p class="norm_text">Привет, ${user.username}.</p>-->
<#--                <div class="search-part">-->
<#--                    <img-->
<#--                            alt=""-->
<#--                            class="serach-img"-->
<#--                            src="https://static.overlay-tech.com/assets/10d973cf-84de-4dd1-9a20-967acd9af517.svg"-->
<#--                    />-->
<#--                    <p class="search-text">Buscar</p>-->
<#--                </div>-->
<#--            </div>-->
<#--            <div class="playlists"></div>-->
<#--        </div>-->


    </div>

</div>
<#list musicList as music>
    <br>
    ${music.name()}
    <audio controls style="width:100%;max-width:600px;">
        <source src="${music.musicPath()}" type="audio/mp3">

    </audio>

</#list>
</div>

<#--<!-- Используйте любой элемент для открытия боковой навигации &ndash;&gt;-->
<#--<span class="navbar-toggler-icon" onclick="openNav()"> open </span>-->
<button onclick="openNav()">
    <img class="profile_menu" src="pokeball.png"/>
</button>


<!-- Добавить все содержимое страницы внутри этого div, если вы хотите, чтобы боковая навигация нажимала содержимое страницы вправо (не используется, если вы хотите, чтобы sidenav сидел поверх страницы -->
<div id="main">
    ...
</div>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
</body>
</html>