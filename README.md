[![Java CI with Maven](https://github.com/kristiania-pgr203-2021/pgr203-exam-Christoffer-Roman/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr203-2021/pgr203-exam-Christoffer-Roman/actions/workflows/maven.yml)
# PGR203 Avansert Java eksamen

## Linker til relevante repositories:
### Tidligere arbeidskrav (som vi hentet noe kode fra)
* https://github.com/kristiania-pgr203-2021/pgr203-innlevering-3-Generoman
* https://github.com/kristiania-pgr203-2021/pgr203-Innlevering-2
### Code Review partnere sitt repository:
* Vi fikk ikke direkte tilgang til repositoryet, men fikk hjelp til å hente inn query parameters fra URL via JavaScript med dette kodeeksempelet:
  ```
    const urlSearchParams = new URLSearchParams(window.location.search);
    const questionId = urlSearchParams.get("questionId");
    ```
 * Denne hjelpen fikk vi av Marie Stigen og Christian Gregersen


## Beskriv hvordan programmet skal testes:

### Testing
* Når serveren kjører, gå til `localhost:8008` i nettleseren, og test følgende:
  * Legg til flere spørsmål av ulike typer
  * Se alle spørsmål
  * Endre flere spørsmål av ulike typer
  * Svar på ett eller flere spørsmål av ulike typer
  * Se alle svar på ett eller flere spørsmål



## Dokumentasjon av bygging og kjøring via CMD
* Bytt directory til pgr203-exam-Christoffer-Roman
* Kjør `mvn package`
* Jar-filen vil ligge i target. Denne kan nå flyttes hvor som helst
* Opprett pgr203.properties med dette innholdet:
  * `dataSource.url=[din database-url]`
  * `dataSource.username=[ditt brukernavn]` - Merk at vi har brukt `username`, og ikke `user`
  * `datasource.password=[ditt passord]`
* Legg `pgr203.properties` i samme directory som jar-filen
* Kjør `java -jar pgr203-exam-Christoffer-Roman.jar`

## Dokumentasjon av kode

* Et noe forenklet sekvensdiagram av hvordan et spørsmål legges til:
![Sekvensdiagram](https://user-images.githubusercontent.com/53780885/141655841-47304e2c-9657-493d-be95-3d0d0a087358.png)

### Overordnet
* Server starter på egen tråd (Main.main()), oppretter controllers for alle mulige paths, venter på request
* HttpWorker-objekt opprettes for hver innkommende request og kjører på ny tråd
* HttpWorker tar imot outputStream fra klient, gjør det om til et HttpRequest-objekt
* HttpWorker sender HttpRequest videre til relevant Controller
  * Hvis path i HttpRequest peker til en HTML-fil:
    * FileController returnerer et HttpResponse-objekt med filen som body
  * Hvis path i HttpRequest peker til et API:
    * Controller sjekker om det er en POST- eller GET-request, og lagrer/oppdaterer eller henter informasjon fra database via tilhørende Dao-objekt
* HttpWorker får et HttpResponse-objekt fra Controller, og sender det i inputStream til klienten

### QuestionsController
* Vi har valgt å beskrive denne fordi det er den mest kompliserte Controller-klassen vi har; de andre følger stort sett samme oppsett, bare enklere
* QuestionsController får inn et HttpRequest-objekt fra HttpWorker
  * Hvis metoden i HttpRequest er GET:
    * Sjekker antall query parameters (hentes fra URL):
      * Hvis det er ingen:
        * Returnerer HTML med alle spørsmål i databasen (via QuestionDao) i message body i et HttpResponse-objekt. Brukes til å se alle spørsmål
      * Hvis det er mer enn én:
        * Returner HTML med input-felt for et spesifikt spørsmål i databasen (via QuestionDao) i message body i et HttpResponse-objekt. Brukes til å endre et spørsmål
      * Hvis det bare er én:
        * Returnerer HTML med et skjult input-felt som inneholder id for det relevante spørsmålet i databasen (via QuestionDao) i message body i et HttpResponse-objekt. Brukes til å knytte et svar til en spørsmål
    * Hvis metoden i HttpRequest er POST:
      * Sjekker hva query parameter “dbAction” er (bestemt av form i HTML-fil)
        * Hvis den er “save”:
          * Kjører post()-metoden, som lagrer et spørsmål i databasen (via QuestionDao) ved hjelp av query parameters, og returnerer et HttpResponse-objekt med kode 303, som redirecter til listen over alle spørsmål
        * Hvis den er "update":
          * Kjører patch()-metoden, som endrer et spørsmål i databasen (via QuestionDao) ved hjelp av query parameters, og returnerer et HttpResponse-objekt med kode 303, som redirecter til listen over alle spørsmål.
        * Hvis den er “multipleAnswers”:
          * Kjører postMultipleAnswers()-metoden, som lagrer spørsmålet og tilhørende svaralternativ i databasen (via QuestionDao) ved hjelp av query parameters, og returnerer et HttpResponse-objekt med kode 303, som redirecter til listen over alle spørsmål
        * Hvis den er “scale”:
          * Kjører postScale()-metoden, som lagrer spørsmålet i databasen med tilhørende svartype (skala 0-5) (via QuestionDao) ved hjelp av query parameters, og returnerer et HttpResponse-objekt med kode 303, som redirecter til listen over alle spørsmål
    * Hvis metoden er noe annet enn GET eller POST:
      * Returnerer et HttpResponse-objekt med kode 405

## Dokumentasjon av prosess



## Vedlegg: Sjekkliste for innlevering

* [x] Dere har lest eksamensteksten
* [ ] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [x] Koden er sjekket inn på github.com/pgr203-2021-repository
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [x] `README.md` inneholder en korrekt link til Github Actions
* [x] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
* [x] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [x] `README.md` inneholder et diagram som viser datamodellen

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [x] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [x] Serveren leser HTML-filer fra JAR-filen slik at den ikke er avhengig av å kjøre i samme directory som kildekoden
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [x] Programmet kan opprette spørsmål og lagrer disse i databasen (påkrevd for bestått)
* [x] Programmet kan vise spørsmål (påkrevd for D)
* [x] Programmet kan legge til alternativ for spørsmål (påkrevd for D)
* [x] Programmet kan registrere svar på spørsmål (påkrevd for C)
* [x] Programmet kan endre tittel og tekst på et spørsmål (påkrevd for B)

### Ekstraspørsmål (dere må løse mange/noen av disse for å oppnå A/B)

* [ ] Før en bruker svarer på et spørsmål hadde det vært fint å la brukeren registrere navnet sitt. Klarer dere å implementere dette med cookies? Lag en form med en POST request der serveren sender tilbake Set-Cookie headeren. Browseren vil sende en Cookie header tilbake i alle requester. Bruk denne til å legge inn navnet på brukerens svar
* [x] Når brukeren utfører en POST hadde det vært fint å sende brukeren tilbake til dit de var før. Det krever at man svarer med response code 303 See other og headeren Location
* [x] Når brukeren skriver inn en tekst på norsk må man passe på å få encoding riktig. Klarer dere å lage en <form> med action=POST og encoding=UTF-8 som fungerer med norske tegn? Klarer dere å få æøå til å fungere i tester som gjør både POST og GET?
* [x] Å opprette og liste spørsmål hadde vært logisk og REST-fult å gjøre med GET /api/questions og POST /api/questions. Klarer dere å endre måten dere hånderer controllers på slik at en GET og en POST request kan ha samme request target?
* [x] Vi har sett på hvordan å bruke AbstractDao for å få felles kode for retrieve og list. Kan dere bruke felles kode i AbstractDao for å unngå duplisering av inserts og updates?
* [x] Dersom noe alvorlig galt skjer vil serveren krasje. Serveren burde i stedet logge dette og returnere en status code 500 til brukeren
* [x] Dersom brukeren går til http://localhost:8080 får man 404. Serveren burde i stedet returnere innholdet av index.html
* [ ] Et favorittikon er et lite ikon som nettleseren viser i tab-vinduer for en webapplikasjon. Kan dere lage et favorittikon for deres server? Tips: ikonet er en binærfil og ikke en tekst og det går derfor ikke an å laste den inn i en StringBuilder
* [x] I forelesningen har vi sett på å innføre begrepet Controllers for å organisere logikken i serveren. Unntaket fra det som håndteres med controllers er håndtering av filer på disk. Kan dere skrive om HttpServer til å bruke en FileController for å lese filer fra disk?
* [ ] Kan dere lage noen diagrammer som illustrerer hvordan programmet deres virker?
* [ ] JDBC koden fra forelesningen har en feil ved retrieve dersom id ikke finnes. Kan dere rette denne?
* [x] I forelesningen fikk vi en rar feil med CSS når vi hadde `<!DOCTYPE html>`. Grunnen til det er feil content-type. Klarer dere å fikse det slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer?
* [ ] Klarer dere å lage en Coverage-rapport med GitHub Actions med Coveralls? (Advarsel: Foreleser har nylig opplevd feil med Coveralls så det er ikke sikkert dere får det til å virke)
* [ ] FARLIG: I løpet av kurset har HttpServer og tester fått funksjonalitet som ikke lenger er nødvendig. Klarer dere å fjerne alt som er overflødig nå uten å også fjerne kode som fortsatt har verdi? (Advarsel: Denne kan trekke ned dersom dere gjør det feil!)
