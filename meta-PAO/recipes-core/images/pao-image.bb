SUMMARY = "A combined image with Qt and vSomeIP applications"
LICENSE = "CLOSED"

inherit core-image

IMAGE_FEATURES += "splash ssh-server-dropbear weston package-management"

IMAGE_INSTALL:append = " \
    qtwayland connman-client bash \
    systemd systemd-analyze systemd-boot \
    weston-init \
    pao \
    vsomeip \
"

IMAGE_ROOTFS_EXTRA_SPACE = "5242880"