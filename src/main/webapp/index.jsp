<!DOCTYPE html>
<html lang="de">
<head>
    <title>Parkhaus</title>
</head>

<style>

    .popup, .popup2{
        width: 50%;
        height: 50%;
        background-color: #f1f1f1;
        border: 1px solid #ccc;
        padding: 20px;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        display: none;

    }

</style>

<body style="width: 100%; margin: 0 auto;max-width: 900px;background-color: #2b5aa0; color:#2b5aa0">
<div style="background-color: #cccccc; padding: 10px; width: 100%">
    <h1 style="text-align: center;background-color: #2b5aa0; color: white;"><span style=" padding:10px;">Willkommen</span></h1>

    <table style="width: 100%">
        <th></th>
        <tr>
            <td width="40%" valign="top">

                <h2 id="Anzahl"></h2>

                <button onclick="anzahlzeigen()">Verf&uuml;gbare Pl&auml;tze anzeigen</button>
            </td>
            <td width="10%">
            </td>

            <td width="40%" valign="top">
                <h2 >&Ouml;ffnungszeit: 06:00 - 24:00 Uhr</h2>


            </td>
        </tr>

    </table>

    <!------------------------------Aktuelle Zeit im Parkhaus------------------------>

    <form action="ParkhausServlet" method="get">
        <label style="padding-right: 10px; font-size: 21px; font-weight: 600">Aktuelle Zeit im Parkhaus</label>
        <input type="datetime-local" id="Zeit" name="aktuelleZeit"  value=""  disabled style="background-color: #2b5aa0; color: white">
        <br>
        <input type="submit" value="Zeit aktualisieren" onclick="ZeitAktualisieren(event)"  style="margin-top: 10px">
        <input type="submit" value="+ 5 Minuten" onclick="Add5Min(event)">

        <input type="submit" value="+ 10 Minuten" onclick="Add10Min(event)">
        <input type="submit" value="+ 1 Stunde" onclick="Add1std(event)">
    </form>
    <p style="border-top:3px solid #2b5aa0"></p>


    <!-----------------------------------Einfahren------------------------------------->

    <p style="margin: 0; font-weight: 600; font-size: 19px">Klicken Sie auf Einfahren, um ein Ticket zu erhalten</p>
    <form action="ParkhausServlet" method="post" >

        <input type="hidden"  name="aktion" value="Parkhaus einfahren">
        <input type="datetime-local" id="EinfahrtZeit" name="EinfahrtZeit" hidden readonly> <!--readonly statt disabled, da bei disabled die Anfrage nicht geschickt wird-->
        <br>
        <input type="submit" value="Einfahren" >
    </form>

    <!-----------------------------------Bezahlen------------------------------------->

    <button style="margin: 10px 0; " onclick="showPopup()">Bezahlen</button>

    <div id="popup" class="popup" >
        <h2>Geben Sie zuerst Ihr Ticket-ID ein!</h2>

        <form action="ParkhausServlet" method="get" target="myIframe">

            <input type="text" id="bezahlen" name="TicketID" value="Geben Sie Ihr Ticket-ID ein">

            <h2 >W&auml;hlen Sie die Uhrzeit des Ausfahrts aus!</h2>

            <input type="datetime-local" id="Ausfahrtzeit" name="Ausfahrtzeit" readonly>

            <br>
            <input type="submit" value="Zeit aktualisieren" onclick="ZeitAktualisieren2(event)"  style="margin-top: 10px">
            <input type="submit" value="+ 5 Minuten" onclick="Add5Min2(event)">

            <input type="submit" value="+ 10 Minuten" onclick="Add10Min2(event)">
            <input type="submit" value="+ 1 Stunde" onclick="Add1std2(event)">


            <br>

            <input type="hidden"  name="aktion" value="bezahlen">
            <button type="submit" style="margin: 10px auto; ">Kosten berechnen</button>

        </form>

        <button onclick="closePopup();" style="margin: 10px auto; ">Abbrechen</button>


    </div>
    <br>

    <!-----------------------------------Verlassen------------------------------------->

    <button  onclick="showPopup2()">Verlassen</button>


    <div id="popup2" class="popup2" >
        <h2>Geben Sie zuerst Ihr Ticket-ID ein!</h2>
        <form action="ParkhausServlet" method="post" target="myIframe">

            <input type="text" id="ParkhausVerlassen" name="TicketID2" value="Geben Sie Ihr Ticket-ID ein">
            <input type="hidden"  name="aktion" value="ParkhausVerlassen">
            <button type="submit" >Verlassen</button>

        </form>

        <button onclick="closePopup2();" style="margin: 10px auto; ">Abbrechen</button>


    </div>

    <!-----------------------------------Aktuelle parkende Autos------------------------------------->

    <p style="margin: 10px 0; font-weight: 600; font-size: 19px">Hier erhalten Sie eine Liste &uuml;ber aktuell parkende Autos</p>

    <form action="ParkhausServlet" method="post">
        <input type="hidden"  name="aktion" value="gueltige Tickets">
        <input type="submit" value="Liste der aktuell parkenden Autos">
    </form>

    <!-----------------------------------Alle Tickets------------------------------------->

    <p style="margin: 10px 0; font-weight: 600; font-size: 19px">Hier erhalten Sie eine Liste &uuml;ber alle gegeben Tickets </p>

    <form action="ParkhausServlet" method="post">
        <input type="hidden"  name="aktion" value="Alle Tickets">
        <input type="submit" value="Alle Tickets">
    </form>



    <p style="margin: 10px 0; font-weight: 600; font-size: 19px">Geben Sie Ihr Ticket-ID ein, um Infos &uuml;ber Ihr Ticket zu erhalten</p>
    <form action="ParkhausServlet" method="get">
        <label for="TicketID" >Informationen &uuml;ber Ihr Ticket</label>
        <input type="hidden"  name="aktion" value="Infos erhalten">

        <input type="text" id="TicketID" name="TicketID" value="Geben Sie Ihr Ticket-ID ein">
        <input type="submit" value="Klicken">
    </form>

</div>



<!------------------------------Funktionen für die aktuelle Zeit im Parkhaus sowie die Einfahrtzeit und Bezahlen------------------------------------>
<script>


    //aktuelle Zeit im local Storage speichern, damit die beim neu Laden der Seit vorhanden bleibt bzw. sofort angezeigt.
    document.addEventListener("DOMContentLoaded", saveTime());
    function saveTime(){
        var time = localStorage.getItem("parkhaus_zeit");

        if(time){
            document.getElementById("Zeit").value = time;
            document.getElementById("EinfahrtZeit").value= time;
        }
    }


    function showPopup() {
        var popup = document.getElementById("popup");
        popup.style.display = "block";
    }

    function closePopup() {
        var popup = document.getElementById("popup");
        popup.style.display = "none";
    }

    function showPopup2() {
        var popup2 = document.getElementById("popup2");
        popup2.style.display = "block";
    }

    function closePopup2() {
        var popup2 = document.getElementById("popup2");
        popup2.style.display = "none";
    }


    function ZeitAktualisieren(event){
        event.preventDefault(); // Verhindert das Standardverhalten des Formulars, also es die Seite wird nicht nach dem Funktionsausführen neu geladen
        var aktu_Zeit = new Date(); // Aktuelles Datum und Uhrzeit
        var dateInput = document.getElementById("Zeit");
        aktu_Zeit.setHours(aktu_Zeit.getHours()+2); //auf die Ziet wird immer 2 Stunde addiert, da die Ziet 2 Stunden zurückgesetzt wird.
        dateInput.value = aktu_Zeit.toISOString().slice(0,16);

        document.getElementById("EinfahrtZeit").value= aktu_Zeit.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= aktu_Zeit.toISOString().slice(0,16);

        localStorage.setItem('parkhaus_zeit', dateInput.value);
    }


    function Add5Min(event) {
        event.preventDefault();
        var dateInput = document.getElementById("Zeit");
        var StringToDate = new Date(dateInput.value); //konvertiere Inhalt von input mit id Zeit zu date-objekt, da Input Inhalte als Text bzw. String schicket
        StringToDate.setMinutes(StringToDate.getMinutes() + 5);
        StringToDate.setHours(StringToDate.getHours()+2);
        dateInput.value = StringToDate.toISOString().slice(0,16);
        document.getElementById("EinfahrtZeit").value= StringToDate.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= StringToDate.toISOString().slice(0,16);
        localStorage.setItem('parkhaus_zeit', dateInput.value);
    }

    function Add10Min(event) {
        event.preventDefault();
        var dateInput = document.getElementById("Zeit");
        var StringToDate = new Date(dateInput.value); //konvertiere Inhalt von input mit id Zeit zu date-objekt
        StringToDate.setMinutes(StringToDate.getMinutes() + 10);
        StringToDate.setHours(StringToDate.getHours()+2);

        dateInput.value = StringToDate.toISOString().slice(0,16);
        document.getElementById("EinfahrtZeit").value= StringToDate.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= StringToDate.toISOString().slice(0,16);

        localStorage.setItem('parkhaus_zeit', dateInput.value);
    }
    function Add1std(event) {
        event.preventDefault();
        var dateInput = document.getElementById("Zeit");
        var StringToDate = new Date(dateInput.value); //konvertiere Inhalt von input mit id Zeit zu date-objekt
        StringToDate.setMinutes(StringToDate.getMinutes() + 60);
        StringToDate.setHours(StringToDate.getHours()+2);

        dateInput.value = StringToDate.toISOString().slice(0,16);
        document.getElementById("EinfahrtZeit").value= StringToDate.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= StringToDate.toISOString().slice(0,16);
        localStorage.setItem('parkhaus_zeit', dateInput.value);
    }



</script>

<!------------------------------Funktionen für die Ausfahrtzeit------------------------------------>
<script>

    function anzahlzeigen(){
        var autos = document.getElementById("Anzahl");
        var anzahl = <%= request.getAttribute("FreiePlaetze") %> ;
        if (anzahl == null)
            anzahl = 10
        autos.innerHTML = "Verf&uuml;gbare Parkpl&auml;tze: " +  anzahl;
    }


</script>



<script>

    function ZeitAktualisieren2(event){
        event.preventDefault(); // Verhindert das Standardverhalten des Formulars, also es die Seite wird nicht nach dem Funktionsausführen neu geladen
        var aktu_Zeit = new Date(); // Aktuelles Datum und Uhrzeit
        var dateInput = document.getElementById("Ausfahrtzeit");
        aktu_Zeit.setHours(aktu_Zeit.getHours()+2); //auf die Ziet wird immer 2 Stunde addiert, da die Ziet 2 Stunden zurückgesetzt wird.
        dateInput.value = aktu_Zeit.toISOString().slice(0,16);

        document.getElementById("EinfahrtZeit").value= aktu_Zeit.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= aktu_Zeit.toISOString().slice(0,16);

    }


    function Add5Min2(event) {
        event.preventDefault();
        var dateInput = document.getElementById("Ausfahrtzeit");
        var StringToDate = new Date(dateInput.value); //konvertiere Inhalt von input mit id Zeit zu date-objekt, da Input Inhalte als Text bzw. String schicket
        StringToDate.setMinutes(StringToDate.getMinutes() + 5);
        StringToDate.setHours(StringToDate.getHours()+2);
        dateInput.value = StringToDate.toISOString().slice(0,16);
        document.getElementById("EinfahrtZeit").value= StringToDate.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= StringToDate.toISOString().slice(0,16);


    }

    function Add10Min2(event) {
        event.preventDefault();
        var dateInput = document.getElementById("Ausfahrtzeit");
        var StringToDate = new Date(dateInput.value); //konvertiere Inhalt von input mit id Zeit zu date-objekt
        StringToDate.setMinutes(StringToDate.getMinutes() + 10);
        StringToDate.setHours(StringToDate.getHours()+2);

        dateInput.value = StringToDate.toISOString().slice(0,16);
        document.getElementById("EinfahrtZeit").value= StringToDate.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= StringToDate.toISOString().slice(0,16);

    }
    function Add1std2(event) {
        event.preventDefault();
        var dateInput = document.getElementById("Ausfahrtzeit");
        var StringToDate = new Date(dateInput.value); //konvertiere Inhalt von input mit id Zeit zu date-objekt
        StringToDate.setMinutes(StringToDate.getMinutes() + 60);
        StringToDate.setHours(StringToDate.getHours()+2);

        dateInput.value = StringToDate.toISOString().slice(0,16);
        document.getElementById("EinfahrtZeit").value= StringToDate.toISOString().slice(0,16);
        document.getElementById("Ausfahrtzeit").value= StringToDate.toISOString().slice(0,16);

    }



</script>



</body>
</html>