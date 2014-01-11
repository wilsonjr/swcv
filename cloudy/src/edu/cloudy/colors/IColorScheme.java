package edu.cloudy.colors;

import edu.cloudy.nlp.Word;

import java.awt.Color;

/**
 * @author spupyrev
 * Nov 28, 2013
 */
public interface IColorScheme
{
    static final Color ORANGE = new Color(230, 85, 13);
    static final Color BLUE = new Color(49, 130, 189);
    static final Color GREEN = new Color(49, 163, 84);;

    static final Color[] bear_down = {
            new Color(204, 0, 51),
            new Color(0, 51, 102) };

    static final Color[] colorbrewer_1 = {
            new Color(166, 206, 227),
            new Color(31, 120, 180),
            new Color(178, 223, 138),
            new Color(51, 160, 44),
            new Color(251, 154, 153),
            new Color(227, 26, 28),
            new Color(253, 191, 111),
            new Color(255, 127, 0),
            new Color(202, 178, 214) };

    static final Color[] colorbrewer_2 = {
            new Color(228, 26, 28),
            new Color(55, 126, 184),
            new Color(77, 175, 74),
            new Color(152, 78, 163),
            new Color(255, 127, 0),
            new Color(255, 255, 51),
            new Color(166, 86, 40),
            new Color(247, 129, 191),
            new Color(153, 153, 153) };

    static final Color[] colorbrewer_3 = {
            new Color(141, 211, 199),
            new Color(255, 255, 179),
            new Color(190, 186, 218),
            new Color(251, 128, 114),
            new Color(128, 177, 211),
            new Color(253, 180, 98),
            new Color(179, 222, 105),
            new Color(252, 205, 229),
            new Color(217, 217, 217) };

    static final Color[] trischeme_1 = {
            new Color(255, 0, 0),
            new Color(0, 153, 153),
            new Color(159, 238, 0),
            new Color(166, 0, 0) };
    static final Color[] trischeme_2 = {
            new Color(0, 255, 255),
            new Color(255, 170, 0),
            new Color(255, 0, 0),
            new Color(0, 99, 99) };
    static final Color[] trischeme_3 = {
            new Color(0, 155, 149),
            new Color(255, 169, 0),
            new Color(253, 0, 6),
            new Color(0, 101, 97) };

    static final Color[] similar_1 = {
            new Color(240, 0, 29),
            new Color(255, 103, 0),
            new Color(19, 0, 131),
            new Color(159, 0, 19) };
    static final Color[] similar_2 = {
            new Color(126, 7, 169),
            new Color(213, 0, 101),
            new Color(66, 18, 175),
            new Color(82, 2, 110) };
    static final Color[] similar_3 = {
            new Color(0, 142, 155),
            new Color(152, 237, 0),
            new Color(166, 54, 3),
            new Color(0, 129, 10) };

    Color getColor(Word word);
}