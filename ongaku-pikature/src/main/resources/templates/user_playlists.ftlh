${user.getUser().login}

<#list musicList as music>
    <br>
    <div>${music.name()}</div>
    <audio controls style="width:100%;max-width:600px;">
        <source src="${music.musicPath()}" type="audio/mp3">
    </audio>

</#list>