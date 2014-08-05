package location;

import groupon.DealFinder;
import groupon.Division;

import java.util.*;

/**
 * User: atscott
 * Date: 11/9/13
 * Time: 1:16 PM
 */
public class DealLocator
{
  public List<Division> FindClosestDivisionsToLocation(double lat, double lng, int numberOfDivisions)
  {
    DealFinder finder = new DealFinder();
    List<Division> divisions = finder.GetDivisions();
    Division[] divisionsOrderedByDistance = orderDivisionsByDistance(lat, lng, divisions);
    return getTopDivisions(numberOfDivisions, divisionsOrderedByDistance);
  }

  private List<Division> getTopDivisions(int numberOfDivisions, Division[] divisionsOrderedByDistance)
  {
    List<Division> closestDivisions = new ArrayList<>();
    for (int i = 0; i < divisionsOrderedByDistance.length && i < numberOfDivisions; i++)
    {
      closestDivisions.add(divisionsOrderedByDistance[i]);
    }
    return closestDivisions;
  }

  private Division[] orderDivisionsByDistance(double lat, double lng, List<Division> divisions)
  {
    Map<Double, Division> divisionDistances = new TreeMap<>();
    for (Division division : divisions)
    {
      double distance = GetUniqueDistanceForDivision(lat, lng, divisionDistances, division);
      divisionDistances.put(distance, division);
    }
    return divisionDistances.values().toArray(new Division[divisionDistances.values().size()]);
  }

  private double GetUniqueDistanceForDivision(double lat, double lng, Map<Double, Division> divisionDistances, Division division)
  {
    double distance = Math.sqrt(Math.pow(lat - division.lat, 2) + Math.pow(lng - division.lng, 2));
    while (divisionDistances.containsKey(distance))
    {
      distance += Double.MIN_VALUE;
    }
    return distance;
  }
}
