package dumbuth;

import java.util.ArrayList;
import java.util.Arrays;

public class Path {

    private Type type = Type.Directory;
    private String name;

    private ArrayList<Path> children;
    private Path destination;
    private String contents;

    private Path(String name) {
        this.name = name;
    }

    public Path(String name, Path... children) {
        type = Type.Directory;
        this.name = name;
        this.children = new ArrayList<>();
        this.children.addAll(Arrays.asList(children));
    }

    public Path(String name, String contents) {
        type = Type.File;
        this.name = name;
        this.contents = contents;
    }

    public Path createLink(String name) {
        Path link = new Path(name);
        link.type = Type.Link;
        link.destination = this;
        return link;
    }

    public Path getDestination() {
        return destination;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public ArrayList<Path> getChildren() {
        return children;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Path find(String name) {
        while (name.startsWith("/"))
            name = name.substring(1);
        if (name.length() == 0) return this;
        String[] split = name.split("/", 2);
        for (Path child : children) {
            if (child.name.equals(split[0])) {
                if (split.length > 1 && child.type == Type.Directory) {
                    return child.find(split[1]);
                }
                return child;
            }
        }
        return null;
    }

    public enum Type {
        Link,
        File,
        Directory
    }

    public static class FileSystem extends Path {

        public static FileSystem createDefault() {
            return new FileSystem(
                    new Path("exceptions",
                            new Path("thing.txt", "")
                    ),
                    new Path("files",
                            new Path("flag.txt", "KAF{alskdnlaskdnalksdnalksndlaksnd}")
                    )
            );
        }

        public FileSystem(Path... children) {
            super("root", new Path("default.txt", "This is the default file for FileSystem initialization."));
            super.children.addAll(Arrays.asList(children));
        }
    }
}
