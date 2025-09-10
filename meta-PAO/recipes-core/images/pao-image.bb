SUMMARY = "A combined image with Qt and vSomeIP applications and CAN (MCP2515)"
LICENSE = "CLOSED"

inherit core-image

IMAGE_FEATURES += "splash ssh-server-dropbear weston package-management"

IMAGE_INSTALL:append = " \
    psplash \
    qtwayland \
    connman-client \
    bash \
    systemd \
    systemd-analyze \
    systemd-boot \
    weston-init \
    pao \
    vsomeip \
    can-utils \
    iproute2 \
    kernel-module-can \
    kernel-module-can-raw \
    kernel-module-can-dev \
    kernel-module-mcp251x \
"

IMAGE_ROOTFS_EXTRA_SPACE = "5242880"

