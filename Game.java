import java.util.*;
import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class Game {

    class FirstFrame extends JFrame implements ActionListener {
        public TextField password;
        public TextField email;
        public JButton button;
        public int indexAccount;

        public FirstFrame(String title, int i) {
            //setez titlul ferestrei
            super(title);
            if(i==1){
                //setez inchiderea ferestrei
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //folosesc GridBagLayout si setez constrangerile pentru fiecare componenta
                GridBagLayout gridBagLayout = new GridBagLayout();
                getContentPane().setLayout(gridBagLayout);
                GridBagConstraints constraints = new GridBagConstraints();
                JLabel authLabel = new JLabel("Introdu datele de autentificare");
                //umplere orizontala
                constraints.fill = GridBagConstraints.HORIZONTAL;
                //pleaca din celula de coordonate 0 si 0
                constraints.gridx = 0;
                constraints.gridy = 0;
                //ocupa 4 celule latime si 2 celule inaltime
                constraints.gridwidth = 4;
                constraints.gridheight = 2;
                //setez constrangerile
                gridBagLayout.setConstraints(authLabel, constraints);
                //adaug componenta
                getContentPane().add(authLabel);
                JLabel emailLabel = new JLabel("email");
                constraints.gridx = 0;
                constraints.gridy = 2;
                constraints.gridwidth = 2;
                constraints.gridheight = 2;
                gridBagLayout.setConstraints(emailLabel, constraints);
                getContentPane().add(emailLabel);
                email = new TextField(10);
                constraints.gridx = 2;
                constraints.gridy = 2;
                constraints.gridwidth = 2;
                constraints.gridheight = 2;
                gridBagLayout.setConstraints(email, constraints);
                getContentPane().add(email);
                JLabel passwordLabel = new JLabel("parola");
                constraints.gridx = 0;
                constraints.gridy = 4;
                constraints.gridwidth = 2;
                constraints.gridheight = 2;
                gridBagLayout.setConstraints(passwordLabel, constraints);
                getContentPane().add(passwordLabel);
                password = new TextField(10);
                constraints.gridx = 2;
                constraints.gridy = 4;
                constraints.gridwidth = 2;
                constraints.gridheight = 2;
                gridBagLayout.setConstraints(password, constraints);
                getContentPane().add(password);
                button = new JButton("Trimite");
                constraints.gridx = 0;
                constraints.gridy = 6;
                constraints.gridwidth = 4;
                constraints.gridheight = 2;
                gridBagLayout.setConstraints(button, constraints);
                getContentPane().add(button);
                //setez dimensiunea pentru maximul ecranului
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                //setez culoarea de background
                getContentPane().setBackground(Color.CYAN);
                button.addActionListener(this);
                //setez fereastra vizibila
                setVisible(true);
            }
            if(i==2){
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLayout(new FlowLayout());
                String[] a=new String[list.get(indexAccount).allCharacters.size()];
                i=0;
                //fac vectorul de string-uri pentru JList
                for(Character character: list.get(indexAccount).allCharacters)
                    a[i++]=String.valueOf("Name: "+character.name+" Level: "+character.level);
                JList listOfCharacters=new JList(a);
                JScrollPane scrollPane=new JScrollPane(listOfCharacters);
                //adaug in scrollPane JList ul si apoi adaug scrollPane la fereastra
                getContentPane().add(scrollPane);
                getContentPane().setBackground(Color.yellow);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setVisible(true);
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //daca actiunea care a generat evenimentul vine de la un buton
            if(actionEvent.getSource() instanceof JButton){
                //daca butonul de la care vine este chiar variabila button
                if(((JButton)((Object)actionEvent.getSource()))==button){
                    //pentru fiecare cont
                    for (Account account : list) {
                        //daca datele introduse in mail si parola sunt corecte
                        if (account.information.getCredentials().getEmail()
                                .compareTo(email.getText()) == 0 &&
                                account.information.getCredentials().getPassword()
                                        .compareTo(password.getText()) == 0){
                            //salvez indexul personajului
                            indexAccount=list.indexOf(account);
                            //inchid fereastra si creez alta noua
                            setVisible(false);
                            getInnerInstance("Lista cu personaje", 2);
                            break;
                        }
                    }
                }
            }
        }
    }

    //Sablon Singleton cu instantiere intarziata
    private static Game game = null;
    public ArrayList<Account> list;
    public Hashtable<Cell.type, ArrayList<String>> map = new Hashtable<>();

    private Game() {

    }

    //se poate instantia un singur obiect Game
    public static Game getInstance() {
        if (game == null)
            game = new Game();
        return game;
    }

    public FirstFrame getInnerInstance(String text, int i) {
        return new FirstFrame(text, i);
    }

    //se incarca datele parsate din JSON
    //pentru fiecare citire incorecta se arunca exceptii
    public void run() throws InvalidCommandException, InformationIncompleteException {
        try {
            int i, j;
            //se parseaza fisierul
            Object fileParser = new JSONParser()
                    .parse(new FileReader("E:\\Java Projects\\TEMA\\src\\accounts.json"));
            JSONObject jo1 = (JSONObject) fileParser;
            //se obtine vectorul de conturi
            JSONArray jsonArray = (JSONArray) jo1.get("accounts");

            //se initializeaza datele
            list = new ArrayList<>(jsonArray.size());
            Account account;
            Credentials credentials;
            String password = "";
            String email = "";
            String name = "";
            String country = "";
            ArrayList<String> favoriteGames;
            int mapsCompleted = 0;
            String characterType = "";
            String characterName = "";
            int level = 0;
            int experience = 0;

            //pentru fiecare cont
            for (Object accountIterator : jsonArray) {
                //se creeaza un cont
                account = new Account();
                j = 0;
                Object accountsParser = new JSONParser().parse(String.valueOf(accountIterator));
                jo1 = (JSONObject) accountsParser;
                //se extrage dictionarul cu credentiale
                Map credentialsParser = (Map) jo1.get("credentials");

                //pentru fiecare obiect din valorile dictionarului
                //se salveaza in obiectul dorit valoarea sa
                for (Object obj : credentialsParser.values()) {
                    if (obj == null)
                        throw new InformationIncompleteException("Date incomplete");
                    if (j % 2 == 0) {
                        password = String.valueOf(obj);
                        j++;
                    } else {
                        email = String.valueOf(obj);
                        j++;
                    }
                }

                //se pun valorile in cadrul obiectului de tip Credentials
                credentials = new Credentials();
                credentials.setPassword(password);
                credentials.setEmail(email);
                name = (String) jo1.get("name");
                country = (String) jo1.get("country");
                if (name == null || country == null)
                    throw new InformationIncompleteException("Date incomplete");

                //se extrage vectorul de jocuri favorite
                JSONArray favoriteGamesParser = (JSONArray) jo1.get("favorite_games");
                favoriteGames = new ArrayList<>(favoriteGamesParser.size());
                for (Object obj : favoriteGames) {
                    if (obj == null)
                        throw new InformationIncompleteException("Date incomplete");
                    //se pun datele in favoriteGames, iar acest obiect este mentinut
                    //sortat cu Collections.sort()
                    favoriteGames.add(String.valueOf(obj));
                    Collections.sort(favoriteGames);
                }

                //se extrag nr de jocuri jucate
                mapsCompleted = Integer.valueOf((String) jo1.get("maps_completed"));

                //se extrage vectorul de caractere
                JSONArray charactersArray = (JSONArray) jo1.get("characters");
                account.allCharacters = new ArrayList<>(charactersArray.size());
                for (Object c : charactersArray) {
                    j = 0;
                    Map characterParser = (Map) c;
                    for (Object obj : characterParser.values()) {
                        //se salveaza in obiectul dorit fiecare valoare pe rand
                        if (obj == null)
                            throw new InformationIncompleteException("Date incomplete");
                        switch (j) {
                            case 0:
                                characterType = String.valueOf(obj);
                                j++;
                                break;
                            case 1:
                                level = Integer.parseInt(String.valueOf(obj));
                                j++;
                                break;
                            case 2:
                                characterName = String.valueOf(obj);
                                j++;
                                break;
                            case 3:
                                experience = Integer.parseInt(String.valueOf(obj));
                                j++;
                                break;
                            default:
                                break;
                        }
                    }
                    //dupa finalizarea citirii vectorului de personaje, se
                    //populeaza vectorul corespunzator din cont cu fiecare personaj
                    //in parte
                    account.allCharacters.add(CharacterFactory
                            .createCharacter(characterType, level, experience, characterName));
                }
                if (name == null || country == null
                        || favoriteGames == null || (Object) mapsCompleted == null)
                    throw new InformationIncompleteException("Date incomplete");
                else {
                    //se instantiaza pas cu pas obiectul information
                    account.information = new Account.Information.InformationBuilder()
                            .credentials(credentials)
                            .name(name)
                            .country(country)
                            .favoriteGames(favoriteGames)
                            .build();
                    account.gamesPlayed = mapsCompleted;
                    list.add(account);
                }
            }

            //se parseaza fisierul cu povesti
            Object storiesParser = new JSONParser()
                    .parse(new FileReader("E:\\Java Projects\\TEMA\\src\\stories.json"));
            jo1 = (JSONObject) storiesParser;
            jsonArray = (JSONArray) jo1.get("stories");
            j = 0;
            ArrayList<String> a1 = new ArrayList<>(20);
            ArrayList<String> a2 = new ArrayList<>(20);
            ArrayList<String> a3 = new ArrayList<>(20);
            ArrayList<String> a4 = new ArrayList<>(20);
            String key = "";
            String value = "";
            for (Object e : jsonArray) {
                Map map2 = (Map) e;
                for (Object obj : map2.values()) {
                    //se pune fiecare valoarea in vectorul corespunzator cheii
                    switch (j % 2) {
                        case 0: {
                            key = String.valueOf(obj);
                            j++;
                            break;
                        }
                        case 1:
                            value = String.valueOf(obj);
                            switch (key) {
                                case "EMPTY":
                                    a1.add(value);
                                    break;
                                case "ENEMY":
                                    a2.add(value);
                                    break;
                                case "SHOP":
                                    a3.add(value);
                                    break;
                                case "FINISH":
                                    a4.add(value);
                                    break;
                                default:
                                    break;
                            }
                            j++;
                            break;
                    }
                }
            }
            //se adauga in dictionar la cheia dorita, fiecare vector de povesti corespunzator
            map.put(Cell.type.EMPTY, a1);
            map.put(Cell.type.ENEMY, a2);
            map.put(Cell.type.SHOP, a3);
            map.put(Cell.type.FINISH, a4);

            Scanner scanner = new Scanner(System.in);
            System.out.println("\n----- W o r l d    o f    M a r c e l -----\n");
            System.out.println("Scrie cifra contului dorit:");
            for (i = 0; i < list.size(); i++)
                System.out.println(String.valueOf(i) + ": " + list.get(i).information.getName());
            int indexAccount = 0;
            String mail = "";
            String parola = "";
            int indexCharacter = 0;

            try {
                if (!scanner.hasNextInt()) {
                    throw new InvalidCommandException
                            ("Trebuie sa introduci o cifra corespunzatoare");
                } else {
                    indexAccount = scanner.nextInt();
                    if (indexAccount < 0 || indexAccount >= list.size())
                        throw new InvalidCommandException
                                ("Trebuie sa introduci o cifra corespunzatoare");
                }
            } catch (InvalidCommandException invalidCommandException) {
                System.err.println("Trebuie sa introduci o cifra corespunzatoare");
                System.exit(1);
            }

            try {
                System.out.println("Scrie credentialele contului");
                System.out.println("Email - "
                        + list.get(indexAccount).information.getCredentials().getEmail());

                mail = scanner.next();
                if (mail.compareTo(list
                        .get(indexAccount).information.getCredentials().getEmail()) != 0)
                    throw new InvalidCommandException
                            ("Trebuie sa introduci mail-ul corespunzator");

                System.out.println("Parola - "
                        + list.get(indexAccount).information.getCredentials().getPassword());

                parola = scanner.next();
                if (parola.compareTo(list
                        .get(indexAccount).information.getCredentials().getPassword()) != 0)
                    throw new InvalidCommandException
                            ("Trebuie sa introduci parola corespunzatoare");
            } catch (InvalidCommandException invalidCommandException) {
                System.err.println("Trebuie sa introduci o cifra corespunzatoare");
                System.exit(1);
            }

            try {
                System.out.println("Scrie cifra personajului dorit:");
                for (i = 0; i < list.get(indexAccount).allCharacters.size(); i++)
                    System.out.println(String.valueOf(i) + ": "
                            + list.get(indexAccount).allCharacters.get(i).getRole());

                if (!scanner.hasNextInt()) {
                    throw new InvalidCommandException
                            ("Trebuie sa introduci o cifra corespunzatoare");
                } else {
                    indexCharacter = scanner.nextInt();
                    if (indexCharacter < 0 || indexCharacter >=
                            list.get(indexAccount).allCharacters.size())
                        throw new InvalidCommandException
                                ("Trebuie sa introduci o cifra corespunzatoare");
                }
            } catch (InvalidCommandException invalidCommandException) {
                System.err.println("Trebuie sa introduci o cifra corespunzatoare");
                System.exit(1);
            }

            int mod = 0;
            try {
                System.out.println("Alege cifra modului de joc dorit:");
                System.out.println("0: Mod text");
                System.out.println("1: Mod interfata grafica");
                if (!scanner.hasNextInt()) {
                    throw new InvalidCommandException
                            ("Trebuie sa introduci cifra 0 sau cifra 1");
                } else {
                    mod = scanner.nextInt();
                    if (mod != 0 && mod != 1)
                        throw new InvalidCommandException
                                ("Trebuie sa introduci cifra 0 sau cifra 1");
                }
            } catch (InvalidCommandException invalidCommandException) {
                System.err.println("Trebuie sa introduci cifra 0 sau cifra 1");
                System.exit(1);
            }

            //se instantiaza tabla de joc hardcodata
            Grid grid = Grid.getInstance(5, 5);
            ArrayList<ArrayList<Cell>> a = Grid.generateMap();
            grid.currentCharacter = list.get(indexAccount).allCharacters.get(indexCharacter);
            grid.current = grid.a.get(0).get(0);
            grid.current.setVisited();

            if (mod == 0)
                try {
                    options(list.get(indexAccount),
                            list.get(indexAccount).allCharacters.get(indexCharacter), map, grid);
                } catch (InvalidCommandException e) {

                }
            else{
                getInnerInstance("Pagina de autentificare", 1);
            }
            scanner.close();

            //scrierea progresului in JSON
            jo1 = (JSONObject) fileParser;
            jsonArray=(JSONArray)jo1.get("accounts");
            JSONObject jo2=((JSONObject)jsonArray.get(indexAccount));
            jo2.put("maps_completed", String.valueOf(list.get(indexAccount).gamesPlayed));
            JSONObject jo3= (JSONObject)((JSONArray)jo2.get("characters")).get(indexCharacter);
            jo3.put("level", String.valueOf(list.get(indexAccount).allCharacters.get(indexCharacter).level));
            jo3.put("experience", Long.valueOf(list.get(indexAccount).allCharacters.get(indexCharacter).experience));
            try (FileWriter f = new FileWriter("E:\\Java Projects\\TEMA\\src\\accounts.json")) {
                f.write(((JSONObject) fileParser).toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void catchException(String command, Scanner scanner) {
        while (true) {
            try {
                //bucla se termina cand se introduce corect caracterul P
                System.out.println("Scrie litera P");
                command = scanner.next();
                if (command.compareTo("P") != 0)
                    throw new InvalidCommandException("Ai gresit litera. Trebuie litera P");
                else
                    break;
            } catch (InvalidCommandException e) {
                System.err.println("Ai gresit litera. Trebuie litera P");
            }
        }
    }

    public void options(Account account, Character character, Hashtable<Cell.type,
            ArrayList<String>> map, Grid grid) throws InvalidCommandException {
        Shop shop = new Shop();
        Enemy enemy = new Enemy();
        Random random = new Random();
        int i, index = 0;
        Scanner scanner = new Scanner(System.in);
        String command = "";

        //se parcurg 3 casute la dreapta
        System.out.println(grid);
        i = 3;
        while ((i--) != 0) {
            catchException(command, scanner);
            grid.goEast();
            story(grid.current);
            grid.current.setVisited();
            if (grid.current.cellElement.toCharacter() != 'S'
                    && grid.current.cellElement.toCharacter() != 'E')
                System.out.println(grid);
        }
        //se cumpara cele 2 potiuni
        index = 2;
        while ((index--) != 0) {
            System.out.println("\t---Bine ai venit la magazin!---");
            for (Potion potion : shop.potions)
                System.out.println(": " + potion);
            System.out.println("\t---Your inventory---\nCoins: "
                    + character.inventory.coins + "\tWeight: " + character.inventory.weight);
            catchException(command, scanner);
            if (character.buyPotion(shop.potions.get(1)))
                character.inventory.addPotion(shop.selectPotion(1));
            System.out.println("Ai cumparat potiunea numarul 1");
        }
        System.out.println(grid);

        //se parcurge o casuta la dreapta si apoi se parcurg 3 casute in jos
        i = 3;
        catchException(command, scanner);
        grid.goEast();
        story(grid.current);
        grid.current.setVisited();
        if (grid.current.cellElement.toCharacter() != 'S'
                && grid.current.cellElement.toCharacter() != 'E')
            System.out.println(grid);
        while ((i--) != 0) {
            catchException(command, scanner);
            grid.goSouth();
            story(grid.current);
            grid.current.setVisited();
            if (grid.current.cellElement.toCharacter() != 'S'
                    && grid.current.cellElement.toCharacter() != 'E')
                System.out.println(grid);
        }
        //aici este bucla de lupta cu inamicul
        System.out.println("\t---In fata ta a aparut un inamic!---");
        System.out.println("\t\t\t\tYou\t\tEnemy");
        System.out.println("  Current life: "
                + character.currentLife + "\t\t  " + enemy.currentLife);
        System.out.println("Current energy: "
                + character.currentEnergy + "\t\t  " + enemy.currentEnergy);
        i = 0;
        index = 3;
        while (character.currentLife > 0 && enemy.currentLife > 0) {
            switch (i) {
                case 0:
                    if (index == 0) {
                        //aici se intra ultima data pentru a se folosi potiunea de viata
                        //iar inamicul ataca normal
                        //in caz ca se introduce alta litera in afara de P,
                        //se intra intr-o bucla
                        catchException(command, scanner);
                        System.out.println("Ai folosit Life Potion");
                        System.out.println("Inamicul te-a atacat normal");
                        character.inventory.potions.get(0).usePotion(character);
                        character.inventory.potions.remove(0);
                        character.receiveDamage(enemy.getDamage());
                        i = 1;
                        System.out.println("\t\t\t\tYou\t\tEnemy");
                        System.out.println("  Current life: "
                                + character.currentLife + "\t\t  " + enemy.currentLife);
                        System.out.println("Current energy: "
                                + character.currentEnergy + "\t\t  " + enemy.currentEnergy);
                        break;
                    }
                    if (index == 1) {
                        //apoi se intra aici pentru a folosi potiunea de mana
                        //iar inamicul ataca normal
                        catchException(command, scanner);
                        System.out.println("Ai folosit Energy Potion");
                        System.out.println("Inamicul te-a atacat normal");
                        character.inventory.potions.get(1).usePotion(character);
                        character.inventory.potions.remove(1);
                        character.receiveDamage(enemy.getDamage());
                        System.out.println("\t\t\t\tYou\t\tEnemy");
                        System.out.println("  Current life: "
                                + character.currentLife + "\t\t  " + enemy.currentLife);
                        System.out.println("Current energy: "
                                + character.currentEnergy + "\t\t  " + enemy.currentEnergy);

                        catchException(command, scanner);
                        System.out.println("Ai folosit abilitatea " + character.abilities.get(0));
                        System.out.println("Inamicul te-a atacat normal");
                        character.useAbility(character.abilities.get(0), enemy);
                        character.receiveDamage(enemy.getDamage());
                        index--;
                        System.out.println("\t\t\t\tYou\t\tEnemy");
                        System.out.println("  Current life: "
                                + character.currentLife + "\t\t  " + enemy.currentLife);
                        System.out.println("Current energy: "
                                + character.currentEnergy + "\t\t  " + enemy.currentEnergy);
                        break;
                    }
                    if (index != 1 && index != 0) {
                        //intai se intra aici pentru a folosi abilitatile
                        catchException(command, scanner);
                        System.out.println("Ai folosit abilitatea " + character.abilities.get(0));
                        System.out.println("Inamicul a folosit abilitatea " + enemy.abilities.get(0));
                        character.useAbility(character.abilities.get(0), enemy);
                        if (enemy.currentEnergy - enemy.abilities.get(0).energyCost >= 0)
                            enemy.useAbility(enemy.abilities.get(0), character);
                        index--;
                        System.out.println("\t\t\t\tYou\t\tEnemy");
                        System.out.println("  Current life: "
                                + character.currentLife + "\t\t  " + enemy.currentLife);
                        System.out.println("Current energy: "
                                + character.currentEnergy + "\t\t  " + enemy.currentEnergy);
                        break;
                    }
                case 1:
                    //in final, toate atacurile vor fi normale pana cand viata cuiva
                    //devine 0 sau mai putin
                    catchException(command, scanner);

                    enemy.receiveDamage(character.getDamage());
                    if (enemy.currentLife <= 0)
                        break;
                    character.receiveDamage(enemy.getDamage());
                    System.out.println("Ai atacat normal");
                    System.out.println("Inamicul a atacat normal");
                    System.out.println("\t\t\t\tYou\t\tEnemy");
                    System.out.println("  Current life: "
                            + character.currentLife + "\t\t  " + enemy.currentLife);
                    System.out.println("Current energy: "
                            + character.currentEnergy + "\t\t  " + enemy.currentEnergy);
                    break;
            }

            //se decide rezultatul
            if (character.currentLife <= 0) {
                System.out.println("Ai pierdut lupta... :(");
                return;
            }
            if (enemy.currentLife <= 0) {
                account.gamesPlayed++;
                System.out.println("Ai castigat lupta! :)");
                if (character.experience + character.currentLife >= 100) {
                    character.level++;
                    character.experience = character.experience + character.currentLife - 100;
                } else {
                    character.experience += character.currentLife;
                }
            }
        }
        System.out.println("Scrie litera P");
        command = scanner.next();
        grid.goSouth();
        story(grid.current);
        grid.current.setVisited();
        System.out.print(grid);
    }

    public void story(Cell cell) {
        Random rand = new Random();
        //se afiseaza povestile pentru fiecare tip de celula din grid

        switch (cell.cellElement.toCharacter()) {
            case 'N':
                System.out.println(map.get(Cell.type.EMPTY)
                        .get(rand.nextInt(map.get(Cell.type.EMPTY).size())));
                break;
            case 'E':
                System.out.println(map.get(Cell.type.ENEMY)
                        .get(rand.nextInt(map.get(Cell.type.ENEMY).size())));
                break;
            case 'S':
                System.out.println(map.get(Cell.type.SHOP)
                        .get(rand.nextInt(map.get(Cell.type.SHOP).size())));
                break;
            case 'F':
                System.out.println(map.get(Cell.type.FINISH)
                        .get(rand.nextInt(map.get(Cell.type.FINISH).size())));
                break;
        }
    }
}

//exceptiile
class InformationIncompleteException extends Exception {
    public InformationIncompleteException(String warning) {
        super(warning);
    }
}

class InvalidCommandException extends Exception {
    public InvalidCommandException(String warning) {
        super(warning);
    }
}

class Account {
    public Information information;
    public ArrayList<Character> allCharacters;
    public int gamesPlayed;

    //sablon Builder
    static class Information {
        private Credentials credentials;
        private ArrayList<String> favoriteGames;
        private String name;
        private String country;

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.favoriteGames = builder.favoriteGames;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public ArrayList<String> getFavoriteGames() {
            return favoriteGames;
        }

        //se instantiaza fiecare obiect din InformationBuilder
        //apoi se returneaza un obiect de tip Information
        //ale carui valori sunt instantiate cu cele deja
        //instantiate din InformationBuilder
        static class InformationBuilder {
            private Credentials credentials;
            private ArrayList<String> favoriteGames;
            private String name;
            private String country;

            public InformationBuilder() {

            }

            public InformationBuilder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public InformationBuilder favoriteGames(ArrayList<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }
}

class Credentials {
    //respectare principiul incapsularii
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class Grid extends ArrayList {
    public static ArrayList<ArrayList<Cell>> a;
    private static Grid grid = null;
    public static int length, width;
    public Character currentCharacter;
    public Cell current;

    //sablon Singleton cu instantiere intarziata
    private Grid(int l, int w) {
        length = l;
        width = w;
    }

    public static Grid getInstance(int l, int w) {
        if (grid == null)
            grid = new Grid(l, w);
        return grid;
    }

    public static ArrayList<ArrayList<Cell>> generateMap() {
        a = new ArrayList(width);
        int i, j;
        for (i = 0; i < width; i++)
            a.add(new ArrayList<Cell>(length));

        for (i = 0; i < length; i++)
            for (j = 0; j < width; j++)
                a.get(i).add(j, new Cell('N', i, j, new Empty()));

        //gridul hardcodat
        a.get(0).get(0).element = 'P';
        a.get(0).get(3).cellElement = new Shop();
        a.get(0).get(3).element = 'S';
        a.get(1).get(3).cellElement = new Shop();
        a.get(1).get(3).element = 'S';
        a.get(2).get(0).cellElement = new Shop();
        a.get(2).get(0).element = 'S';
        a.get(3).get(4).cellElement = new Enemy();
        a.get(3).get(4).element = 'E';
        a.get(4).get(4).cellElement = new Finish();
        a.get(4).get(4).element = 'F';

        return a;
    }

    public void goNorth() {
        if (current.Oy <= 0)
            System.out.println("Nu te poti deplasa acolo.");
        else
            current = a.get(current.Oy - 1).get(current.Ox);
    }

    public void goSouth() {
        if (current.Oy >= width - 1)
            System.out.println("Nu te poti deplasa acolo.");
        else
            current = a.get(current.Oy + 1).get(current.Ox);
    }

    public void goWest() {
        if (current.Ox <= 0)
            System.out.println("Nu te poti deplasa acolo.");
        else
            current = a.get(current.Oy).get(current.Ox - 1);
    }

    public void goEast() {
        if (current.Ox >= length - 1)
            System.out.println("Nu te poti deplasa acolo.");
        else
            current = a.get(current.Oy).get(current.Ox + 1);
    }

    public String toString() {
        String s = "";
        int i, j;
        for (i = 0; i < width; i++) {
            for (j = 0; j < length; j++)
                if (current == a.get(i).get(j))
                    s += "* ";
                else
                    s += a.get(i).get(j).element + " ";
            s += "\n";
        }
        return s;
    }
}

class Cell {
    public char element;
    public int Ox;
    public int Oy;
    public enum type {EMPTY, ENEMY, SHOP, FINISH}
    public CellElement cellElement;
    public boolean visited;

    public Cell(Object element, int i, int j, CellElement cellElement) {
        this.element = (char) element;
        Oy = i;
        Ox = j;
        this.cellElement = cellElement;
        visited = false;
    }

    public void setVisited() {
        visited = true;
    }
}

interface CellElement {
    public char toCharacter();
}

class Empty implements CellElement {
    public char toCharacter() {
        return 'N';
    }
}

class Finish implements CellElement {
    public char toCharacter() {
        return 'F';
    }
}

interface Element {
    public void accept(Visitor visitor);
}

abstract class Entity implements Element {
    public ArrayList<Spell> abilities;
    public int currentLife;
    public int maxLife;
    public int currentEnergy;
    public int maxEnergy;
    public boolean fire;
    public boolean ice;
    public boolean earth;

    public void healLife(int newLife) {
        if (currentLife + newLife <= maxLife)
            currentLife += newLife;
        else
            currentLife = 100;
    }

    public void healEnergy(int newEnergy) {
        if (currentEnergy + newEnergy <= maxEnergy)
            currentEnergy += newEnergy;
        else
            currentEnergy = 100;
    }

    public void useAbility(Spell ability, Entity entity) {
        //daca entitatea are protectie la tipul abilitatii, atunci nu se primeste damage
        if ((ability.getClass() == Fire.class && entity.fire)
                || (ability.getClass() == Earth.class && entity.earth)
                || (ability.getClass() == Ice.class && entity.ice)) {
            currentEnergy -= ability.energyCost;
            abilities.remove(ability);
            return;
        }
        if (currentEnergy - ability.energyCost >= 0) {
            entity.accept(ability);
            currentEnergy -= ability.energyCost;
            abilities.remove(ability);
        } else {
            entity.receiveDamage(getDamage());
        }
    }

    public abstract void receiveDamage(int receivedDamage);

    public abstract int getDamage();

    public abstract String getRole();
}

abstract class Character extends Entity {
    public String name;
    public int Ox;
    public int Oy;
    public Inventory inventory;
    public int experience;
    public int level;
    public int strength;
    public int charisma;
    public int dexterity;

    public boolean buyPotion(Potion potion) {
        if (inventory.weight - potion.getWeigth() >= 0 && inventory.coins - potion.getPrice() >= 0)
            return true;
        else
            return false;
    }
}

//Fabrica de personaje
//sablon Factory
//se intantiaza obiecte ce au tipuri care provin din aceeasi ierarhie
class CharacterFactory {
    public static Character createCharacter(String characterType, int level, int experience, String name) {
        switch (characterType) {
            case "Warrior":
                return new Warrior(level, experience, name);
            case "Mage":
                return new Mage(level, experience, name);
            case "Rogue":
                return new Rogue(level, experience, name);
            default:
                return null;
        }
    }
}

class Warrior extends Character {
    public Random rand = new Random();

    public Warrior(int level, int experience, String name) {
        Random rand = new Random();
        fire = true;
        earth = false;
        ice = false;
        strength = 10 + 2 * level;
        dexterity = level * 2;
        charisma = level * 2;
        inventory = new Inventory();
        maxEnergy = 100;
        currentEnergy = 100;
        maxLife = 100;
        currentLife = 100;
        this.name = name;
        this.level = level;
        this.experience = experience;
        ArrayList<Spell> a = new ArrayList(3);
        a.add(new Fire());
        a.add(new Earth());
        a.add(new Ice());
        abilities = new ArrayList(3);
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
    }

    public void receiveDamage(int receivedDamage) {
        boolean number1, number2;
        number1 = rand.nextBoolean();
        number2 = rand.nextBoolean();
        if ((number1 == true && number2 == true) || (number1 == false && number2 == false))
            currentLife -= receivedDamage / 2;
        else
            currentLife -= receivedDamage;
    }

    public int getDamage() {
        if (rand.nextBoolean())
            return 2 * strength;
        else
            return strength;
    }

    public String getRole() {
        return "Type: Warrior\tName: " + name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Mage extends Character {
    public Random rand = new Random();

    public Mage(int level, int experience, String name) {
        Random rand = new Random();
        fire = false;
        earth = false;
        ice = true;
        charisma = 10 + 2 * level;
        strength = level * 2;
        dexterity = level * 2;
        inventory = new Inventory();
        this.name = name;
        this.level = level;
        this.experience = experience;
        maxEnergy = 100;
        currentEnergy = 100;
        maxLife = 100;
        currentLife = 100;
        ArrayList<Spell> a = new ArrayList(3);
        a.add(new Fire());
        a.add(new Earth());
        a.add(new Ice());
        abilities = new ArrayList(3);
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
    }

    public void receiveDamage(int receivedDamage) {
        boolean number1, number2;
        number1 = rand.nextBoolean();
        number2 = rand.nextBoolean();
        if ((number1 == true && number2 == true) || (number1 == false && number2 == false))
            currentLife -= receivedDamage / 2;
        else
            currentLife -= receivedDamage;
    }

    public int getDamage() {
        Random rand = new Random();
        if (rand.nextBoolean())
            return 2 * charisma;
        else
            return charisma;
    }

    public String getRole() {
        return "Type: Mage\tName: " + name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Rogue extends Character {
    public Random rand = new Random();

    public Rogue(int level, int experience, String name) {
        Random rand = new Random();
        fire = false;
        earth = true;
        ice = false;
        dexterity = 12 + 2 * level;
        charisma = level * 2;
        strength = level * 2;
        inventory = new Inventory();
        this.name = name;
        this.level = level;
        this.experience = experience;
        maxEnergy = 100;
        currentEnergy = 100;
        maxLife = 100;
        currentLife = 100;
        ArrayList<Spell> a = new ArrayList(3);
        a.add(new Fire());
        a.add(new Earth());
        a.add(new Ice());
        abilities = new ArrayList(3);
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
    }

    public void receiveDamage(int receivedDamage) {
        boolean number1, number2;
        number1 = rand.nextBoolean();
        number2 = rand.nextBoolean();
        if ((number1 == true && number2 == true) || (number1 == false && number2 == false))
            currentLife -= receivedDamage / 2;
        else
            currentLife -= receivedDamage;
    }

    public int getDamage() {
        if (rand.nextBoolean())
            return 2 * dexterity;
        else
            return dexterity;
    }

    public String getRole() {
        return "Type: Rogue\tName: " + name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

//Sablon Visitor
//se modeleaza efectul pe care il au abilitatile Ice, Fire, Earth asupra entitatilor
interface Visitor {
    public void visit(Warrior warrior);

    public void visit(Mage mage);

    public void visit(Rogue rogue);

    public void visit(Enemy enemy);
}

abstract class Spell implements Visitor {
    public int damage;
    public int energyCost;
}

class Ice extends Spell {
    public Ice() {
        energyCost = 35;
        damage = 10;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getDamageCost() {
        return damage;
    }

    public void visit(Warrior warrior) {
        warrior.receiveDamage(damage);
    }

    public void visit(Mage mage) {
        mage.receiveDamage(damage);
    }

    public void visit(Rogue rogue) {
        rogue.receiveDamage(damage);
    }

    public void visit(Enemy enemy) {
        enemy.receiveDamage(damage);
    }

    public String toString() {
        return "Ice";
    }
}

class Fire extends Spell {
    public Fire() {
        energyCost = 40;
        damage = 20;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getDamageCost() {
        return damage;
    }

    public void visit(Warrior warrior) {
        warrior.receiveDamage(damage);
    }

    public void visit(Mage mage) {
        mage.receiveDamage(damage);
    }

    public void visit(Rogue rogue) {
        rogue.receiveDamage(damage);
    }

    public void visit(Enemy enemy) {
        enemy.receiveDamage(damage);
    }

    public String toString() {
        return "Fire";
    }
}

class Earth extends Spell {
    public Earth() {
        energyCost = 40;
        damage = 15;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getDamageCost() {
        return damage;
    }

    public void visit(Warrior warrior) {
        warrior.receiveDamage(damage);
    }

    public void visit(Mage mage) {
        mage.receiveDamage(damage);
    }

    public void visit(Rogue rogue) {
        rogue.receiveDamage(damage);
    }

    public void visit(Enemy enemy) {
        enemy.receiveDamage(damage);
    }

    public String toString() {
        return "Earth";
    }
}

class Enemy extends Entity implements CellElement {
    public Random rand = new Random();
    public int damage;

    public Enemy() {
        currentLife = (int) (Math.random() * (100 - 80)) + 80;
        currentEnergy = (int) (Math.random() * (100 - 80)) + 80;
        maxEnergy = 100;
        maxLife = 100;
        fire = rand.nextBoolean();
        ice = rand.nextBoolean();
        earth = rand.nextBoolean();
        ArrayList<Spell> a = new ArrayList(2);
        a.add(new Fire());
        a.add(new Earth());
        a.add(new Ice());
        abilities = new ArrayList(2);
        abilities.add(a.get(rand.nextInt(3)));
        abilities.add(a.get(rand.nextInt(3)));
        damage = 10;
    }

    public void receiveDamage(int receivedDamage) {
        if (rand.nextBoolean())
            currentLife -= receivedDamage;
        else
            return;
    }

    public int getDamage() {
        if (rand.nextBoolean())
            return 2 * damage;
        else
            return damage;
    }

    public char toCharacter() {
        return 'E';
    }

    public String getRole() {
        return "Type: Enemy";
    }

    public void accept(Visitor visitor) {
        Random random = new Random();
        if (rand.nextBoolean())
            visitor.visit(this);
    }
}

class Inventory {
    public ArrayList<Potion> potions = new ArrayList<Potion>(3);
    public int weight;
    public int coins;

    public Inventory() {
        weight = 3;
        coins = 60;
    }

    public void addPotion(Potion potion) {
        potions.add(potion);
        coins -= potion.getPrice();
        weight -= potion.getWeigth();
    }

    public void removePotion(int index) {
        updateWeight(potions.get(index));
        potions.remove(potions.get(index));
    }

    public void updateWeight(Potion potion) {
        weight += potion.getWeigth();
    }
}

interface Potion {
    public void usePotion(Character character);

    public int getPrice();

    public int getHealingValue();

    public int getWeigth();
}

class HealthPotion implements Potion {
    public int price;
    public int healingValue;
    public int weigth;

    public HealthPotion(int price, int healingValue, int weigth) {
        this.price = price;
        this.healingValue = healingValue;
        this.weigth = weigth;
    }

    public void usePotion(Character character) {
        character.healLife(healingValue);
    }

    public int getPrice() {
        return price;
    }

    public int getHealingValue() {
        return healingValue;
    }

    public int getWeigth() {
        return weigth;
    }

    public String toString() {
        return "Health Potion\t" + "Price: " + price
                + "\tHealing Value: " + healingValue + "\tweight: " + weigth;
    }
}

class EnergyPotion implements Potion {
    public int price;
    public int healingValue;
    public int weigth;

    public EnergyPotion(int price, int healingValue, int weigth) {
        this.price = price;
        this.healingValue = healingValue;
        this.weigth = weigth;
    }

    public void usePotion(Character character) {
        character.healEnergy(healingValue);
    }

    public int getPrice() {
        return price;
    }

    public int getHealingValue() {
        return healingValue;
    }

    public int getWeigth() {
        return weigth;
    }

    public String toString() {
        return "Energy Potion\t" + "Price: " + price
                + "\tHealing Value: " + healingValue + "\tweight: " + weigth;
    }
}

class Shop implements CellElement {
    public ArrayList<Potion> potions;

    public Shop() {
        potions = new ArrayList<>(4);
        potions.add(new HealthPotion(10, 30, 1));
        potions.add(new HealthPotion(25, 60, 2));
        potions.add(new EnergyPotion(20, 40, 1));
        potions.add(new EnergyPotion(35, 60, 2));
    }

    public Potion selectPotion(int index) {
        Potion potion = potions.get(index);
        potions.remove(potion);
        return potion;
    }

    public char toCharacter() {
        return 'S';
    }
}

class Test {
    public static void main(String[] args) {
        Game game = Game.getInstance();
        try {
            game.run();
        } catch (InvalidCommandException e1) {
            System.err.println("Trebuie sa introduci datele corect");
        } catch (InformationIncompleteException e2) {
            System.err.println("Date incomplete");
        }
    }
}
