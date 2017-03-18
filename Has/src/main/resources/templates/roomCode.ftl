<#assign fullName = guest.personalData.fullName >
<#assign code = reservation.reservationCode >

<!DOCTYPE html>
<html>
<head>

</head>
<body style="background-color: #BAC2C5;">
<p style="margin: 0px; padding: 2px;">
    Greetings ${fullName} here is your personal access code for the hotel's system: ${code}.
    <br><br>
</p>
</body>
</html>