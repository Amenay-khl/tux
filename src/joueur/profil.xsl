<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 5 octobre 2020, 19:59
    Author     : Yanis
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:pr='http://myGame/tux'>
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
                <title>Profil du joueur</title>
                <link rel="stylesheet" href="style.css"/>
                <!--W3-->    
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <!--W3-->    
                <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"/>

            </head>
            <body>
                <br/>
             
                <!--W3 https://www.w3schools.com/w3css/tryit.asp?filename=tryw3css_images_card-->  
                <div class="w3-container" style="justify-content: center; display: flex;">
                    <div class="w3-card-4" style="width:20%; text-align: center;">
                        <xsl:element name="img">
                            <xsl:attribute name="src">../<xsl:value-of select="pr:profil/pr:avatar"/></xsl:attribute>
                            <xsl:attribute name="width">100%</xsl:attribute>
                        </xsl:element>
                        <div class="w3-container">
                            <h4>
                                <b>
                                    <xsl:value-of select="pr:profil/pr:nom"/>
                                </b>
                            </h4>
                            <p>Né(e) le <xsl:value-of select="pr:profil/pr:anniversaire"/></p>
                        </div>
                    </div>    
                </div>
                <!--W3-->  
                <br/>
                <xsl:apply-templates select="//pr:parties/pr:partie" />
                <br/>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="pr:partie">
        <xsl:variable name="reussi" select="./@trouvé"/>
        <xsl:variable name="niveau" select="./pr:mot/@niveau"/>
        <xsl:variable name="temps" select="./pr:temps/text()"/>
        
        <!--W3 https://www.w3schools.com/w3css/tryit.asp?filename=tryw3css_tables_card -->  
        <div class="w3-container">
            <p>Partie du : <xsl:value-of select="./@date"/></p>

            <table class="w3-table-all w3-card-4">
                <tr>
                    <th width="30%">Mot cherché</th>
                    <th width="25%">Niveau</th>
                    <th width="25%">Temps</th>
                    <th width="25%">Trouvé</th>
                </tr>
                <tr>
                    <td><xsl:value-of select="./pr:mot/text()"/></td><!-- On affiche toujours le mot de la partie -->
                    <xsl:if test="$temps &lt; 0"> <!-- Si temps inférieur à 0 alors partie annulée -->
                        <td><p style="color:orange;">Partie annulée !</p></td>
                        <td><p style="color:orange;">Partie annulée !</p></td>
                        <td><p style="color:orange;">Partie annulée !</p></td>
                    </xsl:if>
                    <xsl:if test="$temps &gt; 0 or not($temps)"> <!-- Si temps supérieur à 0 alors mot trouvé - Si not($temps) alors partie perdu -->
                        <xsl:if test="$niveau &gt; 0 and $niveau &lt; 6">
                            <td>
                                <xsl:value-of select="$niveau"/>
                            </td>
                        </xsl:if>
                        <xsl:if test="$niveau &lt; 1 or $niveau &gt; 5">
                            <td><p style="color:orange;">Niveau incorrect saisi.</p></td>
                        </xsl:if>
                        <!--<td><xsl:value-of select="./pr:temps/text()"/></td> --> 
                        <xsl:if test="not($reussi)">
                            <td>
                                <xsl:value-of select="./pr:temps/text()"/> seconde(s)</td>
                        </xsl:if>
                        <xsl:if test="$reussi">
                            <td>
                                ∞
                            </td>
                        </xsl:if>
                        <xsl:if test="not($reussi)">
                            <td>100%</td>
                        </xsl:if>
                        <xsl:if test="$reussi">
                            <td>
                                <xsl:value-of select="./@trouvé"/>
                            </td>
                        </xsl:if>
                    </xsl:if>
      
      
                </tr>
            </table>
        </div>
        <br/>
        <!--W3-->  
    </xsl:template>
</xsl:stylesheet>