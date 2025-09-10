SUMMARY = "vSomeIP implementation"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

SRC_URI = "file://vsomeip-3.5.5.tar.gz"

S = "${WORKDIR}/vsomeip-3.5.5"

inherit cmake systemd

DEPENDS += "boost dlt-daemon"
RDEPENDS:${PN} += "boost-filesystem boost-system boost-thread"

EXTRA_OECMAKE += "-DENABLE_SIGNAL_HANDLING=1 -DDEFAULT_CONFIGURATION_FOLDER=/etc/vsomeip"

FILES:${PN} += "/usr/bin /usr/etc/vsomeip"