package eu.furcloud_hosting.api.data;

public enum OperatingSystem {

    DEBIAN11("Debian 11.0", "https://cdn.24fire.de/debian.png"),
    UBUNTU2210("Ubuntu 22.10", "https://cdn.24fire.de/ubuntu.png"),
    UBUNTU2204("Ubuntu 22.04", "https://cdn.24fire.de/ubuntu.png"),
    CENTOS7("CentOS 7", "https://cdn.24fire.de/centos.png"),
    WINDOWS2019("Windows 2019 eval", "https://cdn.24fire.de/windows.png");

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public final String label;
    public final String icon;

    OperatingSystem(String label, String icon) {
        this.label = label;
        this.icon = icon;
    }

    public static boolean isOperatingSystemValid(String os) {
        for (OperatingSystem operatingSystem : OperatingSystem.values()) {
            if (operatingSystem.name().equals(os)) {
                return true;
            }
        }
        return false;
    }


}
