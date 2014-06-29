package edu.cloudy.layout;

import edu.cloudy.graph.CycleCoverExtractor;
import edu.cloudy.graph.Edge;
import edu.cloudy.graph.GreedyCycleCoverExtractor;
import edu.cloudy.graph.Vertex;
import edu.cloudy.graph.WordGraph;
import edu.cloudy.layout.overlaps.ForceDirectedUniformity;
import edu.cloudy.layout.packing.ClusterForceDirectedPlacer;
import edu.cloudy.layout.packing.WordPlacer;
import edu.cloudy.nlp.Word;
import edu.cloudy.nlp.WordPair;
import edu.cloudy.utils.Logger;
import edu.cloudy.utils.SWCRectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CycleCoverAlgo extends BaseLayoutAlgo
{
    private WordGraph graph;
    private List<Edge> edgesInMatching;
    private boolean useGreedy = false;

    public CycleCoverAlgo(List<Word> words, Map<WordPair, Double> similarity)
    {
        super(words, similarity);
        this.graph = new WordGraph(words, similarity);
    }

    public void setUseGreedy(boolean useGreedy)
    {
        this.useGreedy = useGreedy;
    }

    @Override
    public void run()
    {
        if (!useGreedy)
        {
            CycleCoverExtractor tme = new CycleCoverExtractor(graph);
            tme.runUndirected();
            edgesInMatching = tme.getMatchedEdges();
        }
        else
        {
            GreedyCycleCoverExtractor tme = new GreedyCycleCoverExtractor(graph);
            tme.run();
            edgesInMatching = tme.getMatchedEdges();
        }

        checkConsistency(edgesInMatching);

        List<List<Vertex>> cycles = getCycles(edgesInMatching);
        int CYCLE_SIZE_LIMIT = 12;
        cycles = breakLongCycles(cycles, CYCLE_SIZE_LIMIT);

        List<BaseLayoutAlgo> cycleAlgos = new ArrayList<BaseLayoutAlgo>();
        for (List<Vertex> c : cycles)
        {
            BaseLayoutAlgo algo = null;

            if (c.size() <= CYCLE_SIZE_LIMIT)
                algo = new SingleCycleAlgo(getCycleWords(c), getCycleWeights(c));
            else
                algo = new SinglePathAlgo(getCycleWords(c), getCycleWeights(c));

            algo.run();
            cycleAlgos.add(algo);
        }

        Logger.println("#cycles: " + cycles.size());
        Logger.println("weight: " + getRealizedWeight());

        WordPlacer wordPlacer = new ClusterForceDirectedPlacer(graph.getWords(), graph.getSimilarities(), cycleAlgos, bbGenerator);

        for (Word w : graph.getWords())
        {
            wordPositions.put(w, wordPlacer.getRectangleForWord(w));
        }

        new ForceDirectedUniformity<SWCRectangle>().run(graph.getWords(), wordPositions);
    }

    private List<List<Vertex>> breakLongCycles(List<List<Vertex>> cycles, int cycleSizeLimit)
    {
        List<List<Vertex>> result = new ArrayList<List<Vertex>>();
        for (List<Vertex> c : cycles)
            if (c.size() <= cycleSizeLimit)
                result.add(c);
            else
                result.addAll(breakLongCycle(c, cycleSizeLimit));

        return result;
    }

    private List<List<Vertex>> breakLongCycle(List<Vertex> c, int cycleSizeLimit)
    {
        List<List<Vertex>> result = new ArrayList<List<Vertex>>();
        List<Vertex> cur = new ArrayList<Vertex>();
        for (Vertex v : c)
        {
            cur.add(v);
            if (cur.size() >= cycleSizeLimit)
            {
                result.add(new ArrayList<Vertex>(cur));
                cur.clear();
            }
        }

        if (cur.size() > 0)
            result.add(new ArrayList<Vertex>(cur));

        return result;
    }

    private List<List<Vertex>> getCycles(List<Edge> edges)
    {
        List<List<Vertex>> result = new ArrayList<List<Vertex>>();

        Map<Vertex, List<Vertex>> next = new HashMap<Vertex, List<Vertex>>();
        for (Vertex v : graph.vertexSet())
            next.put(v, new ArrayList<Vertex>());

        for (Edge edge : edges)
        {
            Vertex u = graph.getEdgeSource(edge);
            Vertex v = graph.getEdgeTarget(edge);
            next.get(u).add(v);
            next.get(v).add(u);
        }

        Set<Vertex> used = new HashSet<Vertex>();
        for (Vertex v : graph.vertexSet())
            if (!used.contains(v))
            {
                List<Vertex> cycle = new ArrayList<Vertex>();
                dfs(v, v, next, used, cycle);

                result.add(cycle);
            }

        return result;
    }

    private void dfs(Vertex v, Vertex parent, Map<Vertex, List<Vertex>> next, Set<Vertex> used, List<Vertex> cycle)
    {
        used.add(v);
        cycle.add(v);

        for (Vertex u : next.get(v))
            if (!u.equals(parent) && !used.contains(u))
                dfs(u, v, next, used, cycle);
    }

    private List<Word> getCycleWords(List<Vertex> cycle)
    {
        List<Word> res = new ArrayList<Word>();

        for (int i = 0; i < cycle.size(); i++)
            res.add(cycle.get(i));

        return res;
    }

    private Map<WordPair, Double> getCycleWeights(List<Vertex> cycle)
    {
        Map<WordPair, Double> res = new HashMap<WordPair, Double>();

        for (int i = 0; i < cycle.size(); i++)
        {
            Vertex now = cycle.get(i);
            Vertex next = cycle.get((i + 1) % cycle.size());

            Edge edge = graph.getEdge(now, next);
            double weight = graph.getEdgeWeight(edge);
            WordPair wp = new WordPair(now, next);
            res.put(wp, weight);
        }

        return res;
    }

    private void checkConsistency(List<Edge> edges)
    {
        // check that we really have cycles
        Map<Vertex, Integer> degree = new HashMap<Vertex, Integer>();
        for (Edge edge : edges)
        {
            Vertex u = graph.getEdgeSource(edge);
            Vertex v = graph.getEdgeTarget(edge);
            int currentU = (degree.containsKey(u) ? degree.get(u) : 0);
            int currentV = (degree.containsKey(v) ? degree.get(v) : 0);

            if (currentU >= 2 || currentV >= 2)
                throw new RuntimeException("not a cycle");

            degree.put(u, currentU + 1);
            degree.put(v, currentV + 1);
        }
    }
    
    public double getRealizedWeight()
    {
        double realizedWeight = 0;
        for (Edge e : edgesInMatching)
            realizedWeight += graph.getEdgeWeight(e);

        return realizedWeight;
    }

}
