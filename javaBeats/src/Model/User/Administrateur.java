package Model.User;

public class Administrateur extends Utilisateur {
    public Administrateur(String login, String motDePasse, String email) {
        super(login, motDePasse, email);
    }

    // L'admin a accès à tout — les méthodes d'admin iront dans le Controller
}