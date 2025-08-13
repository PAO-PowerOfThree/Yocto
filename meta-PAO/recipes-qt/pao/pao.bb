SUMMARY = "This Recipe is for compiling the PAO Qt application"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/PAO-PowerOfThree/Qt.git;branch=patrick;protocol=https"
SRCREV = "1fec0f6e15286f61381e842e0456cfeb66c2acf2"
S = "${WORKDIR}/git/Qt_Cluster"
PV = "1.0"
PR = "r1"

inherit qt6-cmake systemd

DEPENDS += " \
    qtbase \
    qtdeclarative-native \
    qtserialport \
    qtwayland \
    qtserialbus \
    boost \
    vsomeip \
"

RDEPENDS:${PN} += " \
    qtbase \
    qtdeclarative \
    qtserialport \
    qtwayland \
    qtserialbus \
    boost \
    vsomeip \
"

EXTRA_OECMAKE += " \
    -DQT_HOST_PATH=${STAGING_DIR_NATIVE}/usr \
    -DQT_HOST_PATH_CMAKE_DIR=${STAGING_DIR_NATIVE}/usr/lib/cmake \
    -DCMAKE_PREFIX_PATH=${STAGING_DIR_TARGET}/usr/lib/cmake \
    -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_TARGET} \
"

do_install() {
    install -d ${D}${bindir}
    if [ ! -f "${B}/appPaoCluster" ]; then
        echo "Error: appPaoCluster not found in ${B}!" >&2
        exit 1
    fi
    install -m 0755 ${B}/appPaoCluster ${D}${bindir}/appPaoCluster
}

FILES:${PN} += "${bindir}/appPaoCluster"
