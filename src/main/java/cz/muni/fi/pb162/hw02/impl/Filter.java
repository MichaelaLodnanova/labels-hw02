package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.HasLabels;
import cz.muni.fi.pb162.hw02.LabelFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Class represents the filter of labels. Implements LabelFilter interface
 * in witch all methods are described. Filter class inherits from Matcher
 * class.
 * @author Michaela Lodnanova
 */
public class Filter extends Matcher implements LabelFilter{

    /**
     * Constructor of Filter creates an object which represents
     * the expression by which filter should work.
     * @param matchingExpression Expression that matcher will use
     *                          to match with requested set of labels
     */
    public Filter(String matchingExpression) {
        super(matchingExpression);
    }

    @Override
    public Collection<HasLabels> matching(Iterable<HasLabels> labeled) {
        ArrayList<HasLabels> returnArray = new ArrayList<>();
        for (HasLabels label : labeled){
            if (this.matches(label)){
                returnArray.add(label);
            }
        }
        return returnArray;
    }

    @Override
    public Collection<HasLabels> notMatching(Iterable<HasLabels> labeled) {
        ArrayList<HasLabels> returnArray = new ArrayList<>();
        for (HasLabels label : labeled){
            if (!(this.matches(label))){
                returnArray.add(label);
            }
        }
        return returnArray;
    }

    @Override
    public Collection<HasLabels> joined(Iterable<HasLabels> fst, Iterable<HasLabels> snd) {
        Set<HasLabels> setLabels = new HashSet<>(matching(fst));
        setLabels.addAll(matching(snd));
        return setLabels;
    }

    @Override
    public Collection<HasLabels> distinct(Iterable<HasLabels> fst, Iterable<HasLabels> snd) {
        Set<HasLabels> setFst = new HashSet<>(matching(fst));
        Set<HasLabels> setSnd = new HashSet<>(matching(snd));
        setSnd.stream().filter(Predicate.not(setFst::add)).forEach(setFst::remove);
        return setFst;
    }

    @Override
    public Collection<HasLabels> intersection(Iterable<HasLabels> fst, Iterable<HasLabels> snd) {
        Set<HasLabels> setFst = new HashSet<>(matching(fst));
        Set<HasLabels> setSnd = new HashSet<>(matching(snd));
        setFst.retainAll(setSnd);
        return setFst;
    }
}
