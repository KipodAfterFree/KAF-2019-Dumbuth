import quteshell.Quteshell;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    //    private static final int PORT = 5386;
    private static final int PORT = 5387;
    private static final ArrayList<Quteshell> quteshells = new ArrayList<>();

    private static boolean listening = true;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (listening) {
                quteshells.add(new Quteshell(serverSocket.accept()).begin());
            }
        } catch (Exception e) {
            System.out.println("Host - " + e.getMessage());
        }
    }

//
//                    case "help": {
//                        transmitln("List of all possible commands:");
//                        transmitln("hello   welcome     help");
//                        transmitln("history clear       elevate");
//                        transmitln("id      uname       whoami");
//                        transmitln("ln      fsstcr      chmod");
//                        transmitln("nc      shex        echo");
//                        transmitln("ls      cat         exit");
//                        transmitln("rerun   curl        find");
//                        break;
//                    }

    private static class FSPath {
        private int permission = 0;
        private String name = "";
        private FSPath parent = null;
        private boolean isFile = false;
        private ArrayList<FSPath> children = null;
        private String contents = null;

        private FSPath(String name, int permission, boolean isFile) {
            this.isFile = isFile;
            this.permission = permission;
            this.name = name;
        }

        private FSPath(String name, int permission, FSPath... children) {
            this.isFile = false;
            this.permission = permission;
            this.children.addAll(Arrays.asList(children));
            this.name = name;
        }

        private FSPath(String name, int permission, String contents) {
            this.isFile = true;
            this.permission = permission;
            this.contents = contents;
            this.name = name;
        }

        public void setParent(FSPath parent) {
            if (permission >= 2) {
                this.parent = parent;
            }
        }

        public void addChild(FSPath child) {
            if (!isFile) {
                if (permission >= 2) {
                    children.add(child);
                    child.setParent(this);
                }
            }
        }

        public ArrayList<FSPath> getChildren() {
            if (!isFile) {
                if (permission >= 1) {
                    return this.children;
                }
            }
            return null;
        }

        public void setContents(String contents) {
            if (isFile) {
                if (permission >= 2) {
                    this.contents = contents;
                }
            }
        }

        public String getContents() {
            if (isFile) {
                if (permission >= 1) {
                    return this.contents;
                }
            }
            return null;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class FileSystem extends FSPath {

        public static final FileSystem DEFAULT_FILESYSTEM = new FileSystem();

        static {
            DEFAULT_FILESYSTEM.addChild(new FSPath("readme.txt", 3, "Hello there!\nThis is the default filesystem for l00tshell.\nYou are currently viewing path /readme.txt"));
            DEFAULT_FILESYSTEM.addChild(new FSPath("readable.txt", 1, "This file is readonly."));
        }


        private FileSystem() {
            super("Lootshell", 3, false);
        }

        public FSPath find(String path) {
            int index = 0;
            String[] splits = path.split("/");
            FSPath temp = this;
            while (index < splits.length && temp != null) {
                FSPath stemp = null;
                for (FSPath child : temp.getChildren()) {
                    if (child.name.equals(splits[index])) {
                        if (index + 1 < splits.length) {
                            if (!child.isFile)
                                stemp = child;
                            else
                                stemp = null;
                        } else
                            stemp = child;
                    }
                }
                temp = stemp;
            }
            return temp;
        }
    }
}
