<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <link rel="stylesheet" href="mystyle.css">
</head>
<body>

<div class="welcome-page">


    <div class="menu">
        <p class="app-name">OngakuPikature</p>
        <img
                alt=""
                class="user-pic"
                src="https://via.placeholder.com/136x136"
        />
        <p class="user-name">Timur</p>

        <button class="flex-wrapper-one">
            <img
                    alt=""
                    class="run-muz"
                    src="play.svg"
            />
        </button>
        <button class="my-song-fon">
            <p class="norm_text">Мои плейлисты</p>
        </button>
    </div>


    <div class="main-part">
        <div class="header">
            <img
                    alt=""
                    class="conect"
                    src="https://static.overlay-tech.com/assets/5ece3fe1-615a-49ed-8fde-c7559ecc92c3.svg"
            />
            <p class="norm_text">Привет, ${user.username}.</p>
            <div class="search-part">
                <img
                        alt=""
                        class="serach-img"
                        src="https://static.overlay-tech.com/assets/10d973cf-84de-4dd1-9a20-967acd9af517.svg"
                />
                <p class="search-text">Buscar</p>
            </div>
        </div>
        <div class="playlists"></div>
    </div>


</div>


</body>
</html>