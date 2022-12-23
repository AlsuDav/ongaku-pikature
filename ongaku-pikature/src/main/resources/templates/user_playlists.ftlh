${user.getUser().login}

<#list userPlaylists as playlist>
    <br>
    count: ${playlist.songCount()}
    <a class="buttons" href="/${user.getUser().login}/playlists/${playlist.id()}">${playlist.name()}</a>
</#list>