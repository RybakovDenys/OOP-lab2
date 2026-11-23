<?xml version="1.0" encoding="UTF-8"?>
<!--
    Перетворення: список Flower розбивається на підсписки за типом ґрунту (Soil).
    Ім'я елемента підсписку дорівнює значенню Soil.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <!-- Групування методом Мюнха -->
    <xsl:key name="flowers-by-soil" match="Flower" use="Soil"/>

    <xsl:template match="/Flowers">
        <Flowers>
            <xsl:for-each select="Flower[generate-id() = generate-id(key('flowers-by-soil', Soil)[1])]">
                <xsl:sort select="Soil"/>
                <xsl:element name="{Soil}">
                    <xsl:for-each select="key('flowers-by-soil', Soil)">
                        <xsl:sort select="Name"/>
                        <xsl:apply-templates select="." mode="copy"/>
                    </xsl:for-each>
                </xsl:element>
            </xsl:for-each>
        </Flowers>
    </xsl:template>

    <!-- Копіювання елемента без зайвих оголошень просторів імен (xmlns:xsi) -->
    <xsl:template match="*" mode="copy">
        <xsl:element name="{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="copy"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
