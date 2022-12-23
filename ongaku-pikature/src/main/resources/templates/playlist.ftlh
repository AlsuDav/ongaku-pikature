Playlist: ${playlistInfo.name()}

${user.getUser().login}

<#list playlistMusic as music>
    <br>
    <div>${music.name()}</div>
    <audio controls style="width:100%;max-width:600px;">
        <source src="${music.musicPath()}" type="audio/mp3">
    </audio>
    <#if playlistInfo.name() == 'Избранное'>
        <form action="/song/${music.id()}/like?isLike=false" method="post"
              class="form-horizontal">
            <button type="submit" class="btn btn-outline-danger btn-sm" value="Like">DisLike</button>
        </form>
        <#else>
        это другой плейлист: здесь будет удаление песенки
    </#if>
</#list>

