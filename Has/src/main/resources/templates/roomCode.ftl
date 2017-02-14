<#assign fullName = guest.personalData.fullName >
<#assign code = reservation.numberAdults >

<!DOCTYPE html>
<html>
<head>

</head>
<body style="background-color: #BAC2C5;">
<p style="margin: 0px; padding: 2px;">
    Greetings ${fullName} here is the code to your room: ${code}.
    <br><br>
</p>
</body>
</html>