<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="nl">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Qquest Security Awareness</title>

<style>

*{
margin:0;
padding:0;
box-sizing:border-box;
}

body{
display:flex;
justify-content:center;
align-items:center;
min-height:100vh;
background:linear-gradient(180deg,#050816 0%,#0b1026 100%);
font-family:'Segoe UI',sans-serif;
color:#ffffff;
padding:24px;
}

.container{
width:100%;
max-width:900px;
text-align:center;
animation:fadeIn .8s ease-in-out;
}

/* Logo */

.logo{
width:140px;
margin:0 auto 20px auto;
display:block;
border-radius:50%;
box-shadow:0 0 25px rgba(0,0,0,.4);
}

/* Badge */

.badge{
display:block;
margin:0 auto 20px auto;
width:fit-content;
padding:8px 16px;
border-radius:999px;
background:rgba(255,80,0,.12);
border:1px solid rgba(255,80,0,.35);
color:#ff7846;
font-size:.8rem;
letter-spacing:.08em;
text-transform:uppercase;
font-weight:600;
}

/* Titel */

h1{
color:#f8fafc;
font-size:2rem;
font-weight:700;
line-height:1.3;
margin-bottom:18px;
}

/* Intro */

.intro{
max-width:720px;
margin:0 auto 35px auto;
color:#cbd5e1;
font-size:1rem;
line-height:1.7;
}

/* Counter box */

.counter-box{
background:linear-gradient(135deg,rgba(21,28,58,.95),rgba(17,24,39,.95));
border:1px solid rgba(99,102,241,.25);
border-radius:24px;
padding:50px 40px;
box-shadow:
0 0 50px rgba(79,70,229,.16),
0 20px 40px rgba(0,0,0,.45);
margin-bottom:28px;
}

/* Teller */

.count{
font-size:6rem;
font-weight:800;
background:linear-gradient(135deg,#818cf8,#38bdf8);
-webkit-background-clip:text;
-webkit-text-fill-color:transparent;
background-clip:text;
line-height:1;
margin-bottom:14px;
animation:popIn .5s cubic-bezier(.175,.885,.32,1.275);
}

/* Teller tekst */

.label{
color:#cbd5e1;
font-size:1rem;
line-height:1.6;
}

/* Stats */

.stats{
display:grid;
grid-template-columns:repeat(2,1fr);
gap:16px;
margin-bottom:28px;
}

.stat-box{
background:rgba(21,28,58,.75);
border:1px solid rgba(99,102,241,.2);
border-radius:18px;
padding:22px 18px;
box-shadow:0 10px 25px rgba(0,0,0,.25);
}

.stat-number{
font-size:2rem;
font-weight:700;
color:#f8fafc;
margin-bottom:8px;
}

.stat-label{
font-size:.95rem;
color:#cbd5e1;
line-height:1.5;
}

/* Waarschuwing */

.warning{
max-width:760px;
margin:0 auto;
color:#94a3b8;
font-size:.95rem;
line-height:1.7;
}

.warning strong{
color:#f8fafc;
}

/* Footer */

.footer{
margin-top:30px;
font-size:.8rem;
color:#64748b;
}

/* Animaties */

@keyframes fadeIn{
from{opacity:0;transform:translateY(20px);}
to{opacity:1;transform:translateY(0);}
}

@keyframes popIn{
from{transform:scale(.7);opacity:0;}
to{transform:scale(1);opacity:1;}
}

/* Mobile */

@media (max-width:768px){

h1{
font-size:1.6rem;
}

.count{
font-size:4rem;
}

.counter-box{
padding:36px 24px;
}

.intro,
.warning{
font-size:.95rem;
}

.stats{
grid-template-columns:1fr;
}

}

</style>

</head>

<body>

<div class="container">

<img src="Images/Qquest_logo_rond_blauw.png" class="logo" alt="Qquest logo">

<div class="badge">
Qquest Security Awareness
</div>

<h1>
Oh nee, je hebt zojuist een onbekende QR code gescand!
</h1>

<p class="intro">
Gelukkig is het deze keer niet het einde van de wereld, maar volgende keer kan het zo maar
een nep login-pagina zijn of malware dat je telefoon infecteert.
</p>

<div class="counter-box">

<div class="count">
${visitCount}
</div>

<div class="label">
keer gescand. Denk na voordat je een QR code scant en controleer altijd de bron.
</div>

</div>

<div class="stats">
    <div class="stat-box">
        <div class="stat-number">${visitsLast7Days}</div>
        <div class="stat-label">scans in de afgelopen 7 dagen</div>
    </div>

    <div class="stat-box">
        <div class="stat-number">${visitsLast30Days}</div>
        <div class="stat-label">scans in de afgelopen 30 dagen</div>
    </div>
</div>

<p class="warning">
<strong>Voordat je de volgende keer een QR code scanned denk aan deze tips om dit op een veilige manier te doen:</strong><br><br>

<strong>Vermijd het scannen van QR codes</strong><br>
QR codes maken het soms heel makkelijk, maar waar mogelijk kan ze je het best vermijden. Volgende keer dus gewoon aan een collega vragen wat het wifi wachtwoord is.<br><br>

<strong>Scan alleen Scan alleen QR codes uit betrouwbare bronnen</strong><br>
Scan geen QR codes uit onverwachte berichten, op stickers of flyers, en ook niet uit onverwachte pakket- of bezorgmeldingen.<br><br>

<strong>Check de URL voordat je de link opent</strong><br>
Op Android kan je als je met de camera op een QR code richt al de URL van de QR code zien voordat je hier iets mee doet. Ziet de URL er verdacht uit? Gebruik de QR code dan niet.<br><br>

<strong>Vul nooit inloggegevens of betalingsinformatie in via een QR code</strong><br>
Legitieme bedrijven vragen vrijwel nooit naar dit soort informatie via een QR code. Ga zelf naar de website toe als je ergens wil inloggen.</p>

<div class="footer">
© Qquest Security Awareness
</div>

</div>

</body>
</html>
