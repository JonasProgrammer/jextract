package org.openjdk.jextract.impl;

public class LookupConfigBuilder extends ClassSourceBuilder {
	private final String generatedLookup;

	public LookupConfigBuilder(SourceFileBuilder sfb, String runtimeHelperName, String generatedLookup) {
		super(sfb, "public", Kind.CLASS, sfb.className(), null, null, runtimeHelperName);
		this.generatedLookup = generatedLookup;
	}

	public void emit() {
		classBegin();
		appendIndentedLines("static final Arena LIBRARY_ARENA = Arena.ofAuto();");
		appendIndentedLines(generatedLookup);
		appendIndentedLines("""
		    static SymbolLookup SYMBOL_LOOKUP = DEFAULT_LOOKUP;
		
			/**
			* Overrides the symbol lookup used for this library. Must be called before the actual class is loaded.
			* @param newLookupGenerator providing a new lookup or an empty optional, resetting the lookup to default
			* @return previous symbol lookup used
			*/
			public static SymbolLookup overrideLookup(Function<Arena, Optional<SymbolLookup>> newLookupGenerator) {
			    SymbolLookup ret = SYMBOL_LOOKUP;
			    SYMBOL_LOOKUP = newLookupGenerator.apply(LIBRARY_ARENA).orElse(DEFAULT_LOOKUP);
			    return ret;
			}
			""");
		classEnd();
	}
}
