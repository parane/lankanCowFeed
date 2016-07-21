package cow.atuts.com.cowfeed.staticData;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cow.atuts.com.cowfeed.dto.FeedObj;
import cow.atuts.com.cowfeed.dto.MilkObj;
import cow.atuts.com.cowfeed.dto.Result;

/**
 * Created by kasun on 7/16/16.
 */
public class DataGenerator {

    // Table Data
    private static Map<Integer, MilkObj> standardNutritionThreeToThreePointFive;
    private static Map<Integer, MilkObj> standardNutritionThreePointFiveToFour;
    private static Map<Integer, MilkObj> standardNutritionFourToFourPointFive;

    private static Map<String, FeedObj> feedNutritionCatOne;
    private static Map<String, FeedObj> feedNutritionCatTwo;
    private static Map<String, FeedObj> feedNutritionCatThree;

    // Standard Values
    private static Double standardWeightForCaw = 300D;
    private static Double standardTdnVariationFor50kg = 300D;
    private static Double maximumDMIntake = 2.5D;

    private static DataGeneratorHelper helper = new DataGeneratorHelper();


    public static Result calculate(Double bodyWeight,
            Double milkYield ,
            Double fatPercentage,List<String> selectedBulkForage,List<String> selectedSupplementary,List<String> concentrateForage){

        standardNutritionThreeToThreePointFive = helper.getStandardNutritionThreeToThreePointFive();
        standardNutritionThreePointFiveToFour= helper.getStandardNutritionThreePointFiveToFour();
        standardNutritionFourToFourPointFive = helper.getStandardNutritionFourToFourPointFive();

        feedNutritionCatOne = helper.getFeedNutritionCatOne();
        feedNutritionCatTwo = helper.getFeedNutritionCatTwo();
        feedNutritionCatThree = helper.getFeedNutritionCatThree();

        // Out puts
        String bulkForageToGive = "";
        String supplementaryForageToGive = "";



        List<Integer> trialSupplementaryForage = new ArrayList<>();
        addTrialValueToTrialSupplementaryForage(trialSupplementaryForage);

        MilkObj relatedMilkObj = getRelatedMilkObjFromTableData(milkYield, fatPercentage);

        Double tdnValue = relatedMilkObj.getTdn();

        Double bodyWeightDiff = bodyWeight - standardWeightForCaw;

        Double tdnValueForCow = tdnValue + bodyWeightDiff * ( standardTdnVariationFor50kg / 50 );

        String bulkForageKey = generateKeyForGivenData(selectedBulkForage);

        FeedObj feedObj = feedNutritionCatOne.get(bulkForageKey);

        Double relatedDM = feedObj.getDm();

        Double relatedTDN = feedObj.getTdn();

        Double eatableDM = bodyWeight * maximumDMIntake / 100 ;

        Double eatableBulkForageKG = eatableDM * 1000 / relatedDM;

        String supplementaryKey = generateKeyForGivenData(selectedSupplementary);
        FeedObj supplementaryFeed = feedNutritionCatTwo.get(supplementaryKey);

        Double concentrateAmount = 0D;
        int attempts = 0;
        Double trialBulkForage = 0D;

        String concentrateKey = "";

        for (Integer trialSupplementary : trialSupplementaryForage) {

            trialBulkForage = eatableBulkForageKG - trialSupplementary;

            Double totalTDN = ( trialBulkForage * relatedTDN ) + ( supplementaryFeed.getTdn() * trialSupplementary );

            if ( totalTDN - tdnValueForCow >= 0) {

                bulkForageToGive = trialBulkForage.toString();
                supplementaryForageToGive = trialSupplementary.toString();
                break;

            } else {

                if (attempts >= trialSupplementaryForage.size() - 1) {

                    bulkForageToGive = trialBulkForage.toString();
                    supplementaryForageToGive = trialSupplementary.toString();
                    concentrateKey = generateKeyForGivenData(concentrateForage);
                    FeedObj concentrateFeed = feedNutritionCatThree.get(concentrateKey);
                    concentrateAmount = ( tdnValueForCow - totalTDN ) / concentrateFeed.getTdn();
                    break;
                }
                attempts += 1;
            }
        }

        Result result=new Result();

        result.setBulkForage(" Bulk Forage : " +  bulkForageKey + " = " + round(Double.parseDouble(bulkForageToGive), 2) + " kg");
        result.setConcentrate(" Supplementary Forage : " + supplementaryKey + " = " + round(Double.parseDouble(supplementaryForageToGive), 2) + " kg");
        result.setSupplementaryForage(" Concentrate : " + concentrateKey + " = " + round(concentrateAmount, 2) + " kg");

        return result;

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private static String generateKeyForGivenData(List<String> selectedBulkForage) {

        String bulkForageKey = "";
        int i = 0;

        for (String basicKey : selectedBulkForage) {

            bulkForageKey = bulkForageKey + basicKey;

            if ( i != selectedBulkForage.size() - 1) {
                bulkForageKey = bulkForageKey + "_";
            }
            i += 1;
        }
        return bulkForageKey;
    }

    private static MilkObj getRelatedMilkObjFromTableData(Double milkYield, Double fatPercentage) {

        MilkObj relatedMilkObj = null;

        if ( fatPercentage >= 3D && fatPercentage < 3.5D) {
            relatedMilkObj = standardNutritionThreeToThreePointFive.get(milkYield.intValue());
        } else if ( fatPercentage >= 3.5D && fatPercentage < 4D ) {
            relatedMilkObj = standardNutritionThreePointFiveToFour.get(milkYield.intValue());
        }else if ( fatPercentage >= 4D && fatPercentage <= 4.5D ) {
            relatedMilkObj = standardNutritionFourToFourPointFive.get(milkYield.intValue());
        }
        return relatedMilkObj;
    }

    private static void addTrialValueToTrialSupplementaryForage(List<Integer> trialSupplementaryForage) {

        for (int trialValues = 4 ; trialValues < 10 ; trialValues++){
            trialSupplementaryForage.add(trialValues);
        }
    }

    private static void printFeedNutrition(Map<String, FeedObj> feedNutrition) {

        for (Map.Entry<String, FeedObj> entry : feedNutrition.entrySet()) {

            String food = entry.getKey();
            Double tdn = entry.getValue().getTdn();
            Double dm = entry.getValue().getDm();
            System.out.print(food + " TDN : " + tdn + " DM : " + dm);
            System.out.println();
        }
    }

    private static void printStandardNutrition(Map<Integer, MilkObj> standardNutrition) {

        for (Map.Entry<Integer, MilkObj> entry : standardNutrition.entrySet()) {

            Integer liters = entry.getKey();
            Double tdn = entry.getValue().getTdn();
            Double dcp = entry.getValue().getDcp();
            System.out.print(liters + " TDN : " + tdn + " DCP : " + dcp);
            System.out.println("");
        }
    }

}
