${user.getUser().login}

<#list userPlaylists as music>
    <br>
    <div>${music.name()}</div>
    count: ${music.songCount()}


</#list>