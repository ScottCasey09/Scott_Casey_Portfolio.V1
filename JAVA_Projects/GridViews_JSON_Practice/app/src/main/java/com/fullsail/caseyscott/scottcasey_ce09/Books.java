// Casey Scott
// Java 1 - 1704
// Books.Java
package com.fullsail.caseyscott.scottcasey_ce09;


class Books {


    Books(String name, String image) {
        this.name = name;
        this.image = image;
    }

    String getName() {
        return name;
    }

    String getImage() {
        return image;
    }

    private final String name;
    private final String image;

}
