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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="https://use.fontawesome.com/releases/v5.0.8/js/all.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <#--    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">-->
</head>

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

${user.getUser().login}

<#list userPlaylists as playlist>
    <br>
    count: ${playlist.songCount()}
    <a class="playlist" href="/${user.getUser().login}/playlists/${playlist.id()}">${playlist.name()}</a>
</#list>

<form action="/${user.getUser().login}/playlists" method="post"
      class="form-horizontal">
    <input type="text" id="name" name="name" placeholder="name">
    <p><input type="submit" value="create playlist"></p>
</form>

</body>
</html>