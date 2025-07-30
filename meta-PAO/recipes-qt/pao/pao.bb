SUMMARY = "This Recipe is for compiling the PAO Qt application"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/PAO-PowerOfThree/Qt.git;branch=omar;protocol=https"
SRCREV = "b44b46d4db8f4aeea28164085a8c7c77539bc55e"
S = "${WORKDIR}/git/Test_Qt"

inherit qt6-cmake systemd

DEPENDS += " \
    qtbase \
    qtdeclarative-native \
    qtserialport \
    qtwayland \
"

RDEPENDS:${PN} += " \
    qtbase \
    qtdeclarative \
    qtserialport \
    qtwayland \
"

EXTRA_OECMAKE += " \
    -DQT_HOST_PATH=${STAGING_DIR_NATIVE}/usr \
    -DQT_HOST_PATH_CMAKE_DIR=${STAGING_DIR_NATIVE}/usr/lib/cmake \
    -DCMAKE_PREFIX_PATH=${STAGING_DIR_TARGET}/usr/lib/cmake \
    -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_TARGET} \
"

do_install() {
    install -d ${D}${bindir}

    if [ ! -f "${B}/appPAO" ]; then
        echo "Error: appPAO not found in ${B}!" >&2
        exit 1
    fi

    install -m 0755 ${B}/appPAO ${D}${bindir}/appPAO
}

FILES:${PN} += "${bindir}/appPAO"