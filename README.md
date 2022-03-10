---Aspecte generale
    La interfata grafica am facut doar prima fereastra, unde se introduce mail-ul si parola, 
se apasa pe Trimite si se trece la fereastra urmatoare unde se afiseaza lista cu personajele 
caracterului cu care ne-am logat.
    Tin sa mentionez ca am facut bonusul de salvare al progresului in JSON. Este in clasa Game,
la finalul metodei run, si liniile sunt comentate. Se poate verifica functionalitatea prin
decomentarea liniilor.
    Pentru selectarea contului dorit si pentru selectarea personajului se vor introduce cifrele
corespunzatoare, parola si email-ul sunt deja scrise si trebuie copiate pentru logare, iar apoi
se va folosi in joc doar tasta P pentru a se trece mai departe.
    La exceptii se intra intr-o bucla care asteapta introducerea corecta a comenzii doar pentru
jocul efectiv (cand se apasa P). In rest, exceptiile incheie programul si trebuie sa se ia rularea
de la capat.
    Inamicul are 2 abilitati, iar caracterul are 3 abilitati.

---Clasa FirstFrame
    GridBagLayout ma ajuta sa setez Layout-ul sub forma unui table datorita constrangerilor. 
Setez la fiecare componenta gridx si gridy si practic componenta se va aseza incepand de la linia 
gridy si coloanal gridx. Setez si gridwidth pentru a specifica cate celule sa ocupe componenta pe 
latime si setez gridheight pentru numarul de celule ocupate pe inaltime. La fill specific ca umplerea 
sa fie orizontala. Toate componentele le construiesc si le adaug in constructor si la final setez 
fereastra vizibila. Daca mail-ul si parola sunt corecte dupa apasarea butonului Trimite, se trateaza 
evenimentul in metoda actionPerformed. Se verifica daca actiunea care a generat evenimentul vine de 
la un buton, apoi se verifica daca actiunea vine chiar de la variabila button. Se construieste un 
JList dintr-un vector de String-uri ce contine date despre caracterele contului autentificat. Se 
inchide fereastra veche si se afiseaza una noua cu acest JList.

---clasa Game:

    Cu ajutorul sablonului Singleton, se va instantia intarziat un singur obiect de tip Game. Un lucru
util deoarece nu am avea nevoie de mai multe obiecte de acest tip. Asta se realizeaza prin constructorul
privat ce nu poate fi accesat din afara clasei si cu o metoda statica publica ce foloseste constructorul
privat. Se returneaza instantierea obiectului game. Astfel, game nu va mai fi null, si nu se va mai putea
intra pe if pentru a instantia inca un obiect.
    Metoda run() parseaza datele din fisierele JSON si instantiaza obiectele cu valorile acestora. Castez
obiectul fileParser la un obiect de tip JSONObject, apoi extrag fiecare camp dorit cu metoda .get().
Primul lucru obtinut cu get este chiar vectorul de conturi ("accounts"), de aceea ma folosesc de un
JSONArray pentru a lucra cu acest vector. Cu o bucla for-each parcurg fiecare obiect din acest JSONArray.
Se instantiaza un account, caruia i se vor seta valorile campurilor mai tarziu, apoi se va adauga in
lista de conturi. Din acest iterator al buclei for-each se extrage pe rand fiecare camp din fiecare cont.
La credentials m-am folosit de un dictionar pentru, iar apoi am parcurs tot cu for-each fiecare obiect
din cadrul valorilor dictionarului. Cand j-ul este par, inseamna ca se citeste o parola, apoi incrementez
j-ul si urmatoarea valoare citita va fi un mail si tot asa. Creez obiectul de tip credentials si ii setez
valorile cu ajutorul metodelor deoarece aceasta clasa respecta principiul incapsularii. Pentru nume, tara
si jocuri jucate pur si simplu obtin valoarea prin metoda get() ce intoarce ce se afla la cheia data ca
parametru. favoriteGames este un vector de string-uri si cu o bucla for-each accesez fiecare valoarea a sa.
Il mentin sortat cu Collections.sort(). Apoi extrag vectorul de personaje pe care il castez la Map pentru
a accesa doar valorile dictionarului, nu si cheile, intr-o bucla for-each. In functie de valoarea lui j
(0, 1, 2 sau 3), iau pe rand, cu un switch, fiecare valoare dorita (intai tipul caracterului, apoi level,
apoi numele caracterului si apoi experienta), apoi il fac pe j=0 si trec la urmatorul caracter din array.
Dupa finalizarea citirii personajelor, se adauga acestea in array-ul de personaje din account cu ajutorul
sablonului Factory. Daca unul din campurile citite este null (lipseste), se arunca exceptia
InformationIncompleteException si se opreste executia programului. Altfel, se instantiaza fiecare obiect
din account, iar information se instantiaza cu ajutorul sablonului Builder. Se continua pana cand se
instantiaza toate conturile, care se adauga in lista de conturi. Dupa acelasi model de parsare, cu metoda
get(cheie), cu castare la Map si cu bucle for-each, extrag setul de valori din Map, apoi adaug pe rand in
vectorul corespunzator cheii (TYPE: EMPTY, ENEMY, SHOP, FINISH), fiecare valoare. Am avut 4 vectori,
corespunzatorifiecarui tip de celula, pe care i-am adaugat la final in map. Din nou, m-am folosit de
un switch si de incrementarea lui j pentru a contrui tiparul de citire. La toate aceste citiri m-am folosit
de obiecte din care ulterior am obtinut valoarea de String, Integer, cu ajutorul functiilor de forma
String.valueOf() sau Integer.parseInt().
    Acum s-a ajuns la inceperea jocului. Se afiseaza lista de conturi. Se asteapta introducerea cifrei
corespunzatoare contului dorit. Apoi se asteapta scrierea mail-ului si parolei (am afisat valorile ce trebuie
scrise pentru a usura rularea jocului). Ulterior, e nevoie sa se aleaga personajul cu care sa se inceapa
jocul, si in final se alege modul text sau interfata grafica. Pentru citiri gresite, se arunca exceptie de
tipul InvalidCommandException si se opreste rularea programului. Se instantiaza tabla de joc hardcodata si se
intra in joc prin metoda options(). De acum incolo, se apasa doar tasta P pentru a se trece la mutarea urmatoare.
Scenariul este hardcodat. Pentru afisarea in JSON m-am folosit de metoda put(cheie, valoare) pentru a actualiza
datele jucatorului. Scriu efectiv in JSON cu ajutorul FileWriter, si pun obiectul rezultat in urma parsarii
fisierului initial, dar cu valorile modificate.
    Metoda CatchException este pentru metoda options() deoarece se va astepta introducerea corecta a tastei P
daca aceasta va fi introdusa gresit (adica nu se va trece mai departe si nu se va opri rularea programului).
    Metoda options() executa scenariul dorit. Se deplaseaza 3 casute la dreapta, apoi una la dreapta si 3 in
jos. Se cumpara la magazin potiunea numarul 1 de 2 (odata potiune de viata, apoi potiune de energie)
ori (avem 4 potiuni in magazin, de la 0 la 3). Apoi se ajunge la bucla de lupta cu inamicul. Fiecare ataca
pe rand, intai personajul. Prima data se folosesc abilitatile. Ambii folosesc 2 abilitati, apoi personajul
foloseste potiunea de mana si inamicul ataca normal, apoi personajul foloseste ultima sa abilitate si inamicul
ataca normal, apoi personajul foloseste potiunea de viata si inamicul ataca normal. In final, ambii ataca normal
pe rand pana cand unul dintre ei ramane fara viata. Se decide cine castiga, se inregistreaza progresul si se
asteapta citirea literei P pentru a ajunge pe casuta finala daca jucatorul a castigat, altfel se incheie direct
jocul.
    Metoda story() afiseaza random o poveste in functie de tipul celulei si extrage povestea din dictionarul
cu chei de tipul celului si cu valori de tipul unui vector de String-uri.

---Clasele exceptiilor

    Clasele mostenesc Exception si apeleaza constructorul parintelui cu un parametru. Aceste exceptii vor
fi aruncate la citiri gresite sau la informatia incompleta citita din JSON. In general, am tratat exceptiile
in blocuri try-catch si am specificat si langa semnatura metodei in care aceste exceptii sunt aruncate ca vor
fi aruncate exceptii in cadrul ei. (throws exception)

---Clasa Account, Clasa Information si Clasa InformationBuilder

    Clasele Information si InformationBuilder sunt clase statice, incluse in Account, care au grija ca
sablonul de proiectare Builder sa fie indeplinit. Datele clasei din Information sunt aceleasi cu cele
din InformationBuilder, doar ca este nevoie de un obiect de tip InformationBuilder pentru a instantia
datele din Information. Intai se initializeaza datele din InformationBuilder, una cate una, si se
returneaza instanta curenta this pentru a retine ceea ce s-a instantiat si a se continua instantierea
celorlalte campuri. Tot asa pana cand se ajunge la ultima metoda, build(), care returneaza un obiect
de tip Information. Se intra in constructorul clasei Information cu builderul care are deja initializate
toate campurile si se initializeaza toate campurile campului Information. In final, se revine la locul
de unde s-a facut apelul cu obiectul de tip Information.

---Clasa Credentials

    Clasa respecta principiul incapsularii (datele clasei sunt private) si accesul la acestea se face
doar prin metode (de setare sau de obtinere a valorii).

---Clasa Grid

    Deoarece trebuie sa fie privat constructorul acestei clase, am aplicat si aici sablonul Singleton
cu instantiere intarziata. Am creat un obiect de tip vector de vectori de celule, pe care l-am facut
static deoarece in cerinta se cere o metoda statica de creare a tablei de joc (iar aceasta poate
modifica doar datele statice ale clasei). Metoda creeaza hardcodat tabla de joc. Aici sunt si cele
4 metode de deplasare (nord, sud, est, vest), care verifica sa nu se depaseasca marginile tablei de
joc. Functia toString afiseaza matricea in functie de celula pe care se afla in momentul curent
personajul.

---Clasa Cell

    Are o metoda ce seteaza celula curenta ca vizitata si un constructor in care initializeaza
variabilele clasei.

---Interfata CellElement
    Am adaugat doua clase suplimentare, Finish si Empty care implementeaza metoda toCharacter pentru
a returna un caracter care sa diferentieze fiecare tip de celula.

---Clasa Entity

    Contine metodele de vindecare a vietii si a manei, in functie de nivelul curent al vietii si al
manei. De asemenea, aici se afla metoda useAbility(). Aceasta verifica initial daca entitatea are
protectie la tipul abilitatii, iar daca are, nu se scade viata, insa energia se scade si se elimina
abilitatea. Daca este suficienta energie, se va folosi abilitatea, altfel se va ataca normal.

---Clasa Character

    Extinde Entity si modeleaza tipurile de caractere. Functia buyPotion verifica daca este suficient
loc in inventar si daca exista suficienti bani in cont pentru a cumpara potiunea. Intoarce true daca
se poate cumpara, false altfel.


---Clasele Warrior, Mage, Rogue, Enemy

    Extind Character si au un constructor mare in care fiecare variabila este initializata. Cele 3
abilitati pe care le vor avea se genereaza random. Metoda getDamage() returneaza valoarea de damage
in functie de sansa de 50% (pe care am implementat-o cu random.nextBoolean). Metoda de receiveDamage()
decide cu ajutorul a doua variabile boolean, corespunzatoare atributelor secundare, daca valoarea
primita ca damage de personaj va fi injumatatita sau nu. Functia de accept() apeleaza functia de
vizitare cu argumentul entitatii curente (this).

---Interfata Visitor si Interfata Element

    Fac parte din sablonul Visitor. Cele 4 metode de vizitare din Visitor sunt implementate in clasele
Ice, Fire si Earth. In functie de argumentul functiei visit, se decide ce entitate primeste
damage, iar in aceasta metoda de receiveDamage se decide daca entitatea are protectie la abilitatea
folosita. Spell implementeaza Visitor, iar Entity implementeaza Element si ele modeleaza efectul pe
care il au abilitatile asupra entitatilor.

---Clasele Spell, Ice, Fire, Earth

    Se initializeaza valorile damage si energyCost in Ice, Fire si Earth, clasele care mostenesc Spell.
Cele 3 au functii de returnare pentru aceste valori si au cele 4 metode de vizitare, specifice fiecarui
tip de entitate (Warrior, Mage, Rogue, Enemy).

---Clasa Inventory

    Initializeaza in constructor capacitatea (greutatea) inventarului si numarul de monede. Are o metoda
de adaugat potiuni, una de eliminat potiuni si una de actualizare pentru capacitatea inventarului.

---Interfata Potion, Clasele HealthPotion, EnergyPotion

    Cele 2 clase implementeaza interfata Potion (adica metodele acesteia). Se initializeaza in constructor
pretul, valoarea de regenerare si greutatea potiunii.

---Clasa Shop

    In constructor se adauga in lista de potiuni cele 4 potiuni disponibile in magazin. Functia selectPotion()
intoarce potiunea corespunzatoare indexului si o elimina din magazin.

