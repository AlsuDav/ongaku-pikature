<!DOCTYPE html>
<html>
<#import "spring.ftl" as spring />
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<#if error??> <!--if attr error exist,then...-->
    <h1>< 'sign_in.page.error'/></h1>
</#if>

<form action="/signIn" method="post">
    <div class="container" style="margin-left: 40%; margin-top: 5%">
        <div class="row" style="margin-left: 7%">
            <h1>Sign In</h1>
        </div>
<#--        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
        <div class="row" style="margin-top: 4%">
            <input type="email" name="email" required placeholder="Email" size="30">
        </div>
        <div class="row" style="margin-top: 2%">
            <input type="password" name="password" required placeholder="password" size="30">
        </div>
        <div class="row" style="margin-top: 2%">
            <label><input type="checkbox" name="remember-me" style="margin-right: 7px">remember me</label>
        </div>
        <div class="row" style="margin-top: 2%">
            <button type="submit" class="btn btn-default" style="width: 300px">Sign In</button>
        </div>
    </div>
</form>


<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>

</body>
</html>