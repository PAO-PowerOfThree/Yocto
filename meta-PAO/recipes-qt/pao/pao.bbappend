SRC_URI:append = " file://pao.service file://disable-serial-getty.sh file://ip.sh file://can.sh"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "pao.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/pao.service ${D}${systemd_unitdir}/system/pao.service

    install -d ${D}/usr/bin
    install -m 0755 ${WORKDIR}/disable-serial-getty.sh ${D}/usr/bin/disable-serial-getty.sh

    install -m 0755 ${WORKDIR}/ip.sh ${D}/usr/bin/ip.sh
    install -m 0755 ${WORKDIR}/can.sh ${D}/usr/bin/can.sh
}
