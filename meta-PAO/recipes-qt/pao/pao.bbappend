SRC_URI:append = " file://pao.service"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "pao.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pao.service ${D}${systemd_unitdir}/system/pao.service
}