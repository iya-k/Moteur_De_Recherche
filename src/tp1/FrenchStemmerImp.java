package tp2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

import tp1.Word;
import utils.Utils;

public class FrenchStemmerImp implements FrenchStemmer 
{

	    private static final String PATTERN_VOWELS_STRING = "[aeiouyâàëéêèïîôûù]";
	    private static final String PATTERN_VOWELS_2_STRING = PATTERN_VOWELS_STRING + PATTERN_VOWELS_STRING;
	    private static final String PATTERN_UI_VOWELS_STRING = PATTERN_VOWELS_STRING + "[ui]" + PATTERN_VOWELS_STRING;
	    private static final String PATTERN_WITH_Y_STRING = PATTERN_VOWELS_STRING + "[+y]";
	    private static final String PATTERN_WITH_Y_ATER_STRING = "[+y]" + PATTERN_VOWELS_STRING;
	    private static final String PATTERN_QU_STRING = "qu";
	    private static final String PATTERN_REGION_STRING = PATTERN_VOWELS_STRING + "[^aeiouyâàëéêèïîôûù]";
	    private static final String PATTERN_NON_VOWELS_ISSEMENT_STRING = "[^aeiouyâàëéêèïîôûù]issement[s]?";
	    private static final Pattern PATTERN_VOWELS_2 = Pattern.compile(PATTERN_VOWELS_2_STRING);
	    private static final Pattern PATTERN_UI_VOWELS = Pattern.compile(PATTERN_UI_VOWELS_STRING);
	    private static final Pattern PATTERN_WITH_Y = Pattern.compile(PATTERN_WITH_Y_STRING);
	    private static final Pattern PATTERN_WITH_Y_AFTER = Pattern.compile(PATTERN_WITH_Y_ATER_STRING);
	    private static final Pattern PATTERN_WITH_QU = Pattern.compile(PATTERN_QU_STRING);
	    private static final Pattern PATTERN_VOWELS = Pattern.compile(PATTERN_VOWELS_STRING);
	    private static final Pattern PATTERN_NON_VOWELS = Pattern.compile(PATTERN_NON_VOWELS_ISSEMENT_STRING);
	    private static final Pattern PATTERN_REGION = Pattern.compile(PATTERN_REGION_STRING);


	    @Override
	    public String getStemmedWord(String w) {
	        w = vowelsUpperCase(w);
	        Word aWord = findRegions(w);
	        boolean hasStepRemovedSuffixe;
	        boolean specialSufficesFound;
	        boolean doSecondStepA;
	        boolean doSecondStepB = false;
	        boolean doThirdStep = false;
	        boolean doFourthStep = true;


	        hasStepRemovedSuffixe = replaceSuffixesWithInRegion2(aWord, Utils.SUFFIXES_ISMES, Utils.BLANK_STRING);
	        hasStepRemovedSuffixe |= removeOrRemoveSuffixesAti(aWord);
	        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(aWord, Utils.SUFFIXES_LOG, Utils.SUFFIXE_LOG);
	        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(aWord, Utils.SUFFIXES_TIONS, Utils.SUFFIXE_U);
	        hasStepRemovedSuffixe |= replaceSuffixesWithInRegion2(aWord, Utils.SUFFIXES_ENCE, Utils.SUFFIXE_ENT);
	        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesEments(aWord);
	        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesIte(aWord);
	        hasStepRemovedSuffixe |= replaceOrRemoveSuffixesIv(aWord);
	        hasStepRemovedSuffixe |= replaceOrRemoveBaicSuffixes(aWord);
	        specialSufficesFound = removeOrReplaceSpecialSuffixes(aWord);

	        doSecondStepA = specialSufficesFound | !hasStepRemovedSuffixe;

	        if (doSecondStepA) {
	            doSecondStepB = !replaceSuffixesWithInPrecededWithConsonant(aWord, Utils.SUFFIXES_CONJ_I);
	        }
	        if (doSecondStepB) {
	            if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IONS)) {
	                aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_IONS, Utils.BLANK_STRING));
	                doThirdStep = true;
	            }
	            doThirdStep |= replaceSuffixesWithInRegion(aWord, Utils.SUFFIXES_CONJ_E);
	            doThirdStep |= replaceSuffixesPrecededEWithInWordRegion(aWord, Utils.SUFFIXES_CONJ_A);


	        }
	        if (doThirdStep) {
	            doFourthStep = doFourthStep(aWord);
	        }

	        if (doFourthStep) {
	            fourthStep(aWord);
	        }
	        finalStep(aWord);


	        return aWord.getNewWord();

	    }

	    private boolean doFourthStep(Word aWord) {
	        boolean suffixeRemains = true;
	        if (aWord.getNewWord().endsWith("Y")) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 1) + "i");
	            suffixeRemains = false;
	        } else if (aWord.getNewWord().endsWith("ç")) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 1) + "c");
	            suffixeRemains = false;

	        }
	        return suffixeRemains;
	    }

	    private void fourthStep(Word aWord) {
	        if (aWord.getNewWord().endsWith("s") && !Utils.SUFFIXE_AIOUES.contains(aWord.getNewWord().charAt(aWord.getNewWord().length() - 2) + Utils.BLANK_STRING)) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 1));
	        }

	        if (aWord.getNewWord().endsWith("ion")) {

	            if ("ts".contains(aWord.getNewWord().charAt(aWord.getNewWord().length() - 4) + Utils.BLANK_STRING)) {
	                aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 4));
	            } else {
	                aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3));
	            }
	        }

	        replaceSuffixesWithInRegion2(aWord, Utils.SUFFIXES_IER, "i");
	        if (aWord.getNewWord().endsWith("e")) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 1));
	        }
	    }

	    private void finalStep(Word aWord) {
	        if (aWord.getNewWord().endsWith(Utils.SUFFIXE_ENN)
	                || aWord.getNewWord().endsWith(Utils.SUFFIXE_ONN)
	                || aWord.getNewWord().endsWith(Utils.SUFFIXE_ETT)
	                || aWord.getNewWord().endsWith(Utils.SUFFIXE_ELL)
	                || aWord.getNewWord().endsWith(Utils.SUFFIXE_EILL)
	                ) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 1));
	        }

	        aWord.setNewWord(aWord.getNewWord().toLowerCase());
	    }

	    private boolean replaceOrRemoveBaicSuffixes(Word aWord) {
	        boolean hasStepRemovedSuffixe;
	        hasStepRemovedSuffixe = false;
	        if (aWord.getNewWord().endsWith(Utils.SUFFIXE_EAUX)) {
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_EAUX, Utils.SUFFIXE_EAU));
	            hasStepRemovedSuffixe = true;
	        }
	        if (aWord.getNewWord().endsWith(Utils.SUFFIXE_AUX) && aWord.getRegionfirst().endsWith(Utils.SUFFIXE_AUX)) {
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_AUX, "al"));
	            hasStepRemovedSuffixe = true;

	        }
	        if (aWord.getRegionfirst().endsWith(Utils.SUFFIXE_EUSES)) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 5));
	            hasStepRemovedSuffixe = true;

	        } else if (aWord.getRegionfirst().endsWith(Utils.SUFFIXE_EUSE)) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 4));
	            hasStepRemovedSuffixe = true;

	        } else if (aWord.getRegionfirst().endsWith(Utils.SUFFIXE_EUSES)) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 5) + Utils.SUFFIXE_EUX);
	            hasStepRemovedSuffixe = true;

	        } else if (aWord.getRegionfirst().endsWith(Utils.SUFFIXE_EUSE)) {
	            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 4) + Utils.SUFFIXE_EUX);
	            hasStepRemovedSuffixe = true;

	        }

	        Matcher matcher = PATTERN_NON_VOWELS.matcher(aWord.getRegionfirst());
	        if (matcher.find()) {
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_ISSEMENTS, Utils.BLANK_STRING));
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_ISSEMENT, Utils.BLANK_STRING));
	            hasStepRemovedSuffixe = true;
	        }

	        return hasStepRemovedSuffixe;
	    }

	    private boolean removeOrReplaceSpecialSuffixes(Word aWord) {
	        Matcher matcher;
	        boolean specialSufficesFound = false;

	        if (aWord.getRegion().endsWith(Utils.SUFFIXE_AMMENT)) {
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_AMMENT, "ant"));
	            specialSufficesFound = true;
	        }
	        if (aWord.getRegion().endsWith(Utils.SUFFIXE_EMMENT)) {
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_EMMENT, Utils.SUFFIXE_ENT));
	            specialSufficesFound = true;
	        }


	        matcher = PATTERN_NON_VOWELS.matcher(aWord.getRegion());
	        if (matcher.find()) {
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_MENTS, Utils.BLANK_STRING));
	            aWord.setNewWord(aWord.getNewWord().replace(Utils.SUFFIXE_MENT, Utils.BLANK_STRING));
	            specialSufficesFound = true;
	        }
	        return specialSufficesFound;
	    }

	    private Word findRegions(String word) {

	        Matcher vowels2 = PATTERN_VOWELS_2.matcher(word);
	        Matcher vowels = PATTERN_VOWELS.matcher(word);
	        Matcher region = PATTERN_REGION.matcher(word);


	        Word aWord = new Word(word);
	        if (word.startsWith(Utils.PREFIX_TAP) || word.startsWith(Utils.PREFIX_COL) || word.startsWith(Utils.PREFIX_PAR)) {
	            aWord.setRegion(word.substring(3));
	        }


	        if (vowels2.find()) {
	            aWord.setRegion(word.substring(3));
	        } else if (vowels.find()) {

	            if (vowels.start() == 0 && vowels.find()) {
	                aWord.setRegion(word.substring(vowels.end()));
	            } else {
	                aWord.setRegion(word.substring(vowels.end()));
	            }
	        }

	        if (region.find()) {
	            aWord.setRegionfirst(word.substring(region.end()));
	            if (region.find()) {
	                aWord.setRegionSecond(word.substring(region.end()));
	            }

	        }

	        return aWord;
	    }

	    private String vowelsUpperCase(String word) {

	        char[] wordArray = word.toCharArray();
	        Matcher uiVowels = PATTERN_UI_VOWELS.matcher(word);
	        Matcher y = PATTERN_WITH_Y.matcher(word);
	        Matcher yAfter = PATTERN_WITH_Y_AFTER.matcher(word);
	        Matcher qu = PATTERN_WITH_QU.matcher(word);

	        while (uiVowels.find()) {
	            wordArray[uiVowels.start() + 1] = Character.toUpperCase(wordArray[uiVowels.start() + 1]);
	        }
	        while (y.find()) {
	            wordArray[y.start() + 1] = Character.toUpperCase(wordArray[y.start() + 1]);
	        }
	        while (yAfter.find()) {
	            wordArray[yAfter.start()] = Character.toUpperCase(wordArray[yAfter.start()]);
	        }

	        while (qu.find()) {
	            wordArray[qu.start() + 1] = Character.toUpperCase(wordArray[qu.start() + 1]);
	        }
	        return new String(wordArray);
	    }

	    private boolean replaceSuffixesWithInRegion2(Word aWord, List<String> suffixes, String replaceWith) {
	        for (String suffixe : suffixes) {
	            if (aWord.getRegionSecond().endsWith(suffixe)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, replaceWith));
	                return true;
	            }
	        }
	        return false;
	    }

	    private boolean replaceSuffixesWithInPrecededWithConsonant(Word aWord, List<String> suffixes) {
	        for (String suffixe : suffixes) {
	            if (aWord.getNewWord().endsWith(suffixe)
	                    && !PATTERN_VOWELS_STRING.contains(aWord.getNewWord().charAt(aWord.getNewWord().length()
	                    - (suffixe.length() - 1)) + Utils.BLANK_STRING)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, Utils.BLANK_STRING));
	                return true;
	            }
	        }
	        return false;
	    }

	    private boolean replaceSuffixesWithInRegion(Word aWord, List<String> suffixes) {
	        for (String suffixe : suffixes) {
	            if (aWord.getRegionSecond().endsWith(suffixe)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, Utils.BLANK_STRING));
	                return true;
	            }
	        }
	        return false;
	    }

	    private boolean replaceSuffixesPrecededEWithInWordRegion(Word aWord, List<String> suffixes) {
	        for (String suffixe : suffixes) {
	            if (aWord.getNewWord().endsWith(suffixe)) {
	                if (aWord.getNewWord().charAt(aWord.getNewWord().length() - suffixe.length()) == 'e') {
	                    aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - suffixe.length() + 1));

	                } else {
	                    aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - suffixe.length()));
	                }
	                return true;
	            }
	        }
	        return false;
	    }

	    private boolean removeOrRemoveSuffixesAti(Word aWord) {
	        for (String suffixe : Utils.SUFFIXES_AT) {
	            if (aWord.getRegionSecond().endsWith(suffixe)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, Utils.BLANK_STRING));
	                if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IC)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_IC + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2));
	                    } else {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2) + Utils.SUFFIXES_iqU);
	                    }

	                }
	                return true;
	            }
	        }
	        return false;
	    }

	    private boolean replaceOrRemoveSuffixesIv(Word aWord) {
	        for (String suffixe : Utils.SUFFIXES_IV) {
	            if (aWord.getRegionSecond().endsWith(suffixe)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, Utils.BLANK_STRING));
	                if (aWord.getNewWord().endsWith(Utils.SUFFIXE_AT)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_AT + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2));
	                    }
	                    if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IC)) {
	                        if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_IC + suffixe)) {
	                            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2));
	                        } else {
	                            aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2) + Utils.SUFFIXES_iqU);
	                        }

	                    }

	                }
	                return true;
	            }
	        }
	        return false;
	    }


	    private boolean replaceOrRemoveSuffixesIte(Word aWord) {
	        for (String suffixe : Utils.SUFFIXES_IT) {
	            if (aWord.getRegionSecond().endsWith(suffixe)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, Utils.BLANK_STRING));
	                if (aWord.getNewWord().endsWith(Utils.SUFFIXE_ABIL)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_ABIL + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3));
	                    } else {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3) + Utils.SUFFIXE_ABL);
	                    }
	                } else if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IC)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_IC + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2));
	                    } else {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2) + Utils.SUFFIXES_iqU);
	                    }
	                } else if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IV)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_IV + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2));
	                    }
	                }
	                return true;
	            }
	        }
	        return false;
	    }

	    private boolean replaceOrRemoveSuffixesEments(Word aWord) {
	        for (String suffixe : Utils.SUFFIXES_EMENT) {
	            if (aWord.getRegionSecond().endsWith(suffixe)) {
	                aWord.setNewWord(aWord.getNewWord().replace(suffixe, Utils.BLANK_STRING));
	                if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IV)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXE_IV + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 2));
	                    }
	                } else if (aWord.getNewWord().endsWith(Utils.SUFFIXES_EUS)) {
	                    if (aWord.getRegionSecond().endsWith(Utils.SUFFIXES_EUS + suffixe)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3));
	                    } else if (aWord.getRegionfirst().contains(Utils.SUFFIXES_EUS)) {
	                        aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3) + Utils.SUFFIXES_EUX);
	                    }
	                } else if (aWord.getNewWord().endsWith(Utils.SUFFIXE_ABL + suffixe) || aWord.getNewWord().endsWith(Utils.SUFFIXES_iqU + suffixe)) {
	                    aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3));
	                } else if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IER + suffixe)) {
	                    aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 3));
	                } else if (aWord.getNewWord().endsWith(Utils.SUFFIXE_IERE + suffixe)) {
	                    aWord.setNewWord(aWord.getNewWord().substring(0, aWord.getNewWord().length() - 4));
	                }
	                return true;
	            }
	        }
	        return false;
	    }
}
