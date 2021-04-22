<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : dico.xsl
    Created on : 29 septembre 2020, 16:41
    Author     : Yanis
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:ns1='http://myGame/tux'>
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->

    <xsl:template match="/">
        <html>
            <head>
                <title> Test </title >
            </head>
            <body>
                <h1> Contenu dictionnaire </h1>
                Il y a <xsl:value-of select = "count(//ns1:mot)"/> mots.
                <xsl:apply-templates select="ns1:dictionnaire/ns1:mot">
                    <xsl:sort select="." order="ascending"/>
                </xsl:apply-templates>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match = "ns1:mot" >
        <p> MOT = <xsl:value-of select ="."/> </p>
    </xsl:template>

</xsl:stylesheet>
