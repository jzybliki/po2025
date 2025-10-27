package animals;

public class Parrot extends Animal {

    public Parrot(String name) {
        super(name, 2);
    }

    @Override
    public String getDescription() {
        return "Papuga o imieniu " + name + " ma " + legs + " nogi.";
    }
}
