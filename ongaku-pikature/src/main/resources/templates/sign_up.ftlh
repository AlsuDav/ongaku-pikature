<!DOCTYPE html>
<html> <!--xmlns:https="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/html">-->
<#import "spring.ftl" as spring />
<head>

    <meta charset="UTF-8">
    <title>Sign Up</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-2.2.4.js" charset="utf-8"></script>
    <script>
        // function sendCode() {
        //     $.ajax({
        //         url: "/signUp/sendConfirmCode",
        //         data: {
        //             "phone": $('#phone').val()
        //         },
        //         success: function (code) {
        //             $('#code').attr('value', ''+ code);
        //         }
        //     })
        // }

        function signUp() {
            $.ajax({
                type: "POST",
                url: "/signUp",
                csrf: false,
                data: {
                    // name: $('#name').val(),
                    email: $('#email').val(),
                    password: $('#password').val(),
                    phoneNumber: $('#phoneNumber').val(),
                    // confirmCode: $('#confirmCode').val(),
                    // code: $('#code').val()
                },
                success: function (msg) {
                    if (msg == 'ok') {
                        window.location.href = "/signIn"
                    } else {
                        alert('wrong code, try again')
                    }
                }
            })
        }
    </script>
</head>
<body>

<form action="/signUp" method="post" >
<#--      oninput='password2.setCustomValidity(password.value !== password2.value ? "passvalue" : "")'>-->
    <div class="container" style="margin-left: 40%; margin-top: 5%">
        <div class="row" style="margin-left: 2%">
            <h1>Sign Up</h1>
        </div>

        <div class="row" style="margin-top: 2%">
            <input type="email" id="email" name="email" required placeholder="Email" size="30">
        </div>
        <div class="row" style="margin-top: 2%">
            <input type="password" id = "password" name="password" required placeholder="password" size="30">
        </div>
        <div class="row" style="margin-top: 2%">
            <input type="password" name="password2" required placeholder="password again" size="30">
        </div>
        <div class="row" style="margin-top: 2%">
            <input type="tel" id="phoneNumber" name="phoneNumber" placeholder="phone" size="30" pattern="[+]{7}[0-9]{11,14}" required>

        </div>






        <div class="row" style="margin-top: 2%">
            <input type="button" onclick=signUp() class="btn btn-default" value="Submit" style="width: 255px"/>

        </div>

    </div>

</form>

<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>

<#--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>

</body>
</html>