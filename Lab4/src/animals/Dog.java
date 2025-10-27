package animals;

public class Dog extends Animal {

    public Dog(String name) {
        super(name, 4);
    }

    @Override
    public String getDescription() {
        return "Pies o imieniu " + name + " ma " + legs + " nogi.";
    }
}
