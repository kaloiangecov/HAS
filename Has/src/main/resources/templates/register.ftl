<#assign fullName = guest.personalData.fullName >
<#assign numberAdults = reservation.numberAdults >
<#assign numberChildren = reservation.numberChildren >
<#assign startDate = reservation.startDate >
<#assign endDate = reservation.endDate >
<#assign price = reservation.price >
<#assign code = "HARDCODEDCODE!R@5!%@DWSAF" >
<#assign moreInformationUrl = "HTTP//FIXTHISURL.COM" >

<#if reservation.allInclusive == true>
    <#assign reservationIncludes = "All inclusive" >
<#else>
    <#assign reservationIncludes = "" >
</#if>

<#if reservation.breakfast == true>
    <#assign reservationIncludes = reservationIncludes + " breakfast" >
<#else>
    <#assign reservationIncludes = reservationIncludes >
</#if>

<#if reservation.dinner == true>
    <#assign reservationIncludes = reservationIncludes + " dinner" >
<#else>
    <#assign reservationIncludes = reservationIncludes >
</#if>

<!DOCTYPE html>
<html>
<head>

</head>
<body style="background-color: #BAC2C5;">
<table
        style="padding: 10px; background-color: white; font-family: Calibri, Candara, Segoe, 'Segoe UI', Optima, Arial, sans-serif; float: left; width: 100%; border-spacing: 10px;">

    <tr style="padding: 2px; font-size: 36px; clear: both;" width="100%">
        <td colspan="8" style="text-align: center;" width="100%"><p
                style="margin: 0px; padding: 2px; align: center">Hotel Name Goes Here</p>
        </td>
    </tr>

    <tr style="clear: both; padding: 1rem; background-color: #F4EFEF">
        <td colspan="8" width="100%"><p
                style="margin: 0px; padding: 2px;">Greetings ${fullName} thank you for registering in our hotel.
            <br><br></p>
    </tr>

    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">Here Goes Some Hotel Info</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">Some More Hotel Info</td>
    </tr>
    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">Your reservation includes</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">${reservationIncludes}</td>
    </tr>

    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">Number of adults</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">${numberAdults}</td>
    </tr>
    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">Number of children</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">${numberChildren}</td>
    </tr>

    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">Check-In</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">${startDate}</td>
    </tr>
    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">Check-Out</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">${endDate}</td>
    </tr>

    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">Price for the stay</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #F4EFEF; font-weight: bold; color: #4783A8;">${price}</td>
    </tr>

    <tr>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">Here is the code for the stay</td>
        <td colspan="2" width="25%"
            style="padding: 10px; clear: both; background-color: #e9f3fb; font-weight: bold;">${code}</td>
    </tr>

    <tr>
        <td colspan="8" width="100%"><p
                style="color: #4783A8; clear: both; margin: 0px; padding: 10px 2px;">Here you can find more information
        ${moreInformationUrl}</p></td>
    </tr>
</table>
<table style="padding: 3px;">
    <tr colspan="8" width="100%">
        <p style="margin: 0px; padding: 2px; clear: both;">
        <td style="color: white">HAS©</td>
        </p>
    </tr>
</table>
</body>
</html>