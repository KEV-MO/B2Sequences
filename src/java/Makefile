JCLASSPATH = .
OUT_DIRECTORY = .

VERIFIER = verifier
UTIL = util
FUNCTION = function
RANGE = range
STUPID = stupid

COMPILE_ARGS = -d $(OUT_DIRECTORY) -cp $(JCLASSPATH)

B2P_JAVA = $(wildcard *.java)
VERIFIER_JAVA = $(wildcard $(VERIFIER)/*.java)
UTIL_JAVA = $(wildcard $(UTIL)/*.java)
FUNCTION_JAVA = $(wildcard $(FUNCTION)/*.java)
RANGE_JAVA = $(RANGE)/RangeSet.java $(RANGE)/Range.java
UTIL_JAVA = $(UTIL)/FileUtility.java $(UTIL)/Search.java

B2P_CLASS = $(patsubst %.java,%.class,$(B2P_JAVA))
VERIFIER_CLASS = $(patsubst %.java,%.class,$(VERIFIER_JAVA))
UTIL_CLASS = $(patsubst %.java,%.class,$(UTIL_JAVA))
FUNCTION_CLASS = $(patsubst %.java,%.class,$(FUNCTION_JAVA))
RANGE_CLASS = $(patsubst %.java,%.class,$(RANGE_JAVA))

JAVACC = javac $< $(COMPILE_ARGS)

MIAN_CHOWLA = mianChowla.seq

DEPENDENCIES = $(UTIL)/CommonUtility.class $(FUNCTION)/ToDoubleFunction.class $(VERIFIER)/SidonVerifier.class $(UTIL)/SequenceUtility.class $(UTIL)/FileUtility.class $(FUNCTION)/FunctionUtility.class

all: OptimalSequenceSearch.class GreedySequence.class $(RANGE)/IntRangeSet.class Driver.class Verifier.class

oss: OptimalSequenceSearch.class
	java OptimalSequenceSearch --maximum=150 --bestcount=5 --function=invert --sequence $(MIAN_CHOWLA) --initialElements=15

gs: GreedySequence.class
	java GreedySequence --maximum=150 --function=invert --sequence $(MIAN_CHOWLA) --initialElements=5 --maximumElements=25

poss: PrunedOptimalSequenceSearch.class
	java PrunedOptimalSequenceSearch --maximum=565 --bestcount=10 --function=invert --sequence $(MIAN_CHOWLA) --initialElements=15

wcb: WorstCaseBalance.class
	java WorstCaseBalance 200

OptimalSequenceSearch.class: OptimalSequenceSearch.java $(DEPENDENCIES)
	$(JAVACC)

PrunedOptimalSequenceSearch.class: PrunedOptimalSequenceSearch.java OptimalSequenceSearch.class
	$(JAVACC)

GreedySequence.class: GreedySequence.java $(DEPENDENCIES)
	$(JAVACC)

$(STUPID)/WorstCaseBalance.class: $(STUPID)/WorstCaseBalance.java Verifier.class $(RANGE)/RangeUtility.class
	$(JAVACC)

Driver.class: Driver.java Search.class
	$(JAVACC)

Search.class: Search.java Skip.class
	$(JAVACC)

Verifier.class: Verifier.java
	$(JAVACC)

Skip.class: Skip.java
	$(JAVACC)

$(FUNCTION_CLASS): $(FUNCTION)/%.class: $(FUNCTION)/%.java
	$(JAVACC)

$(RANGE)/IntRangeSet.class: $(RANGE)/IntRangeSet.java $(RANGE)/RangeSet.class $(RANGE)/IntRange.class
	$(JAVACC)

$(RANGE)/IntRange.class: $(RANGE)/IntRange.java $(RANGE)/Range.class
	$(JAVACC)

$(RANGE)/RangeUtility.class: $(RANGE)/RangeUtility.java $(RANGE)/IntRangeSet.class
	$(JAVACC)

$(RANGE_CLASS): $(RANGE)/%.class: $(RANGE)/%.java
	$(JAVACC)

$(UTIL)/CommonUtility.class: $(UTIL)/CommonUtility.java $(UTIL)/SequenceUtility.class $(UTIL)/FileUtility.class
	$(JAVACC)

$(UTIL)/SequenceUtility.class: $(UTIL)/SequenceUtility.java $(FUNCTION)/FunctionUtility.class $(FUNCTION)/ToDoubleFunction.class $(FUNCTION)/ToBigDecimalFunction.class
	$(JAVACC)

$(UTIL_CLASS): $(UTIL)/%.class: $(UTIL)/%.java $(FUNCTION)/ToDoubleFunction.class
	$(JAVACC)

$(VERIFIER)/SidonVerifier.class: $(VERIFIER)/SidonVerifier.java $(VERIFIER)/AbstractSequenceVerifier.class $(UTIL)/Search.class
	$(JAVACC)

$(VERIFIER)/AbstractSequenceVerifier.class: $(VERIFIER)/AbstractSequenceVerifier.java $(VERIFIER)/SequenceVerifier.class
	$(JAVACC)

$(VERIFIER)/SequenceVerifier.class: $(VERIFIER)/SequenceVerifier.java
	$(JAVACC)

# $(B2P_CLASS): $(B2P)/%.class: %.java
#	javac $^ $(COMPILE_ARGS)

# $(VERIFIER_CLASS): $(VERIFIERC)/%.class: $(VERIFIER)/%.java
#	javac $^ $(COMPILE_ARGS)

# $(UTIL_CLASS): $(UTILC)/%.class: $(UTIL)/%.java
#	javac $^ $(COMPILE_ARGS)

clean:
	rm *.class
	rm $(VERIFIER)/*.class
	rm $(UTIL)/*.class
	rm $(FUNCTION)/*.class
	rm $(RANGE)/*.class
	rm $(STUPID)/*.class
