${user.login}

${musicAddedData.isLiked()}

<img src="${musicAddedData.neuroPicturePath()}" alt="poster">

<br>
<div>${musicMainData.name()}</div>
<audio controls style="width:100%;max-width:600px;">
    <source src="${musicMainData.musicPath()}" type="audio/mp3">
</audio>

<#if user??>
    <form action="/neuro_image/generate" method="post"
          class="form-horizontal">
        <button type="submit" class="btn btn-outline-danger btn-sm" value="Like">Like</button>
    </form>
</#if>