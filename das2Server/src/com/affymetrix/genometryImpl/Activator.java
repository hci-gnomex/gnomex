// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

import com.affymetrix.genometryImpl.operator.FindJunctionOperator;
import com.affymetrix.genometryImpl.operator.GraphMultiplexer;
import com.affymetrix.genometryImpl.operator.XorOperator;
import com.affymetrix.genometryImpl.operator.UnionOperator;
import com.affymetrix.genometryImpl.operator.SumOperator;
import com.affymetrix.genometryImpl.operator.RatioOperator;
import com.affymetrix.genometryImpl.operator.ProductOperator;
import com.affymetrix.genometryImpl.operator.NotOperator;
import com.affymetrix.genometryImpl.operator.MinOperator;
import com.affymetrix.genometryImpl.operator.MedianOperator;
import com.affymetrix.genometryImpl.operator.MeanOperator;
import com.affymetrix.genometryImpl.operator.MaxOperator;
import com.affymetrix.genometryImpl.operator.PowerTransformer;
import com.affymetrix.genometryImpl.operator.LogTransform;
import com.affymetrix.genometryImpl.operator.InverseLogTransform;
import com.affymetrix.genometryImpl.operator.InverseTransformer;
import com.affymetrix.genometryImpl.operator.IntersectionOperator;
import com.affymetrix.genometryImpl.operator.ExclusiveBOperator;
import com.affymetrix.genometryImpl.operator.ExclusiveAOperator;
import com.affymetrix.genometryImpl.operator.DiffOperator;
import com.affymetrix.genometryImpl.operator.SummaryOperator;
import com.affymetrix.genometryImpl.operator.DepthOperator;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.operator.CopySequenceOperator;
import com.affymetrix.genometryImpl.operator.CopyMismatchOperator;
import com.affymetrix.genometryImpl.operator.CopyGraphOperator;
import com.affymetrix.genometryImpl.operator.CopyAnnotationOperator;
import com.affymetrix.genometryImpl.operator.CopyAlignmentOperator;
import com.affymetrix.genometryImpl.operator.ComplementSequenceOperator;
import com.affymetrix.genometryImpl.operator.Operator;
import com.affymetrix.genometryImpl.util.ServerTypeI;
import com.affymetrix.genometryImpl.event.GenericActionHolder;
import com.affymetrix.genometryImpl.event.GenericAction;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.common.ExtensionPointListener;
import com.affymetrix.common.ExtensionPointHandler;
import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import java.util.Dictionary;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.thread.WaitHelperI;
import com.affymetrix.common.CommonUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;

public class Activator implements BundleActivator
{
    protected BundleContext bundleContext;
    
    public void start(final BundleContext _bundleContext) throws Exception {
        this.bundleContext = _bundleContext;
        if (CommonUtils.getInstance().isExit(this.bundleContext)) {
            return;
        }
        this.bundleContext.registerService((Class)WaitHelperI.class, (Object)CThreadHolder.getInstance(), (Dictionary)null);
        this.initFileTypeHandlers();
        this.initGenericActions();
        this.initServerTypes();
        this.initOperators();
    }
    
    public void stop(final BundleContext _bundleContext) throws Exception {
    }
    
    private void initFileTypeHandlers() {
        final ExtensionPointHandler<FileTypeHandler> extensionPoint = (ExtensionPointHandler<FileTypeHandler>)ExtensionPointHandler.getOrCreateExtensionPoint(this.bundleContext, (Class)FileTypeHandler.class);
        extensionPoint.addListener((ExtensionPointListener)new ExtensionPointListener<FileTypeHandler>() {
            public void removeService(final FileTypeHandler fileTypeHandler) {
                FileTypeHolder.getInstance().removeFileTypeHandler(fileTypeHandler);
            }
            
            public void addService(final FileTypeHandler fileTypeHandler) {
                FileTypeHolder.getInstance().addFileTypeHandler(fileTypeHandler);
            }
        });
    }
    
    private void initGenericActions() {
        final ExtensionPointHandler<GenericAction> extensionPoint = (ExtensionPointHandler<GenericAction>)ExtensionPointHandler.getOrCreateExtensionPoint(this.bundleContext, (Class)GenericAction.class);
        extensionPoint.addListener((ExtensionPointListener)new ExtensionPointListener<GenericAction>() {
            public void addService(final GenericAction genericAction) {
                GenericActionHolder.getInstance().addGenericAction(genericAction);
            }
            
            public void removeService(final GenericAction genericAction) {
                GenericActionHolder.getInstance().removeGenericAction(genericAction);
            }
        });
    }
    
    private void initServerTypes() {
        ExtensionPointHandler.getOrCreateExtensionPoint(this.bundleContext, (Class)ServerTypeI.class);
        this.bundleContext.registerService((Class)ServerTypeI.class, (Object)ServerTypeI.LocalFiles, (Dictionary)null);
        this.bundleContext.registerService((Class)ServerTypeI.class, (Object)ServerTypeI.QuickLoad, (Dictionary)null);
        this.bundleContext.registerService((Class)ServerTypeI.class, (Object)ServerTypeI.DAS, (Dictionary)null);
        this.bundleContext.registerService((Class)ServerTypeI.class, (Object)ServerTypeI.DAS2, (Dictionary)null);
    }
    
    private void initOperators() {
        ExtensionPointHandler.getOrCreateExtensionPoint(this.bundleContext, (Class)Operator.class);
        this.bundleContext.registerService((Class)Operator.class, (Object)new ComplementSequenceOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new CopyAlignmentOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new CopyAnnotationOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new CopyGraphOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new CopyMismatchOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new CopySequenceOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new DepthOperator(FileTypeCategory.Alignment), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new DepthOperator(FileTypeCategory.Annotation), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new SummaryOperator(FileTypeCategory.Annotation), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new SummaryOperator(FileTypeCategory.Alignment), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new DiffOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new ExclusiveAOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new ExclusiveBOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new IntersectionOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new InverseTransformer(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new InverseLogTransform(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new InverseLogTransform(2.718281828459045), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new InverseLogTransform(2.0), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new InverseLogTransform(10.0), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new LogTransform(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new LogTransform(2.718281828459045), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new LogTransform(2.0), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new LogTransform(10.0), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new PowerTransformer(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new PowerTransformer(0.5), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new MaxOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new MeanOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new MedianOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new MinOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new NotOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new ProductOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new RatioOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new SumOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new UnionOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new XorOperator(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new GraphMultiplexer(), (Dictionary)null);
        this.bundleContext.registerService((Class)Operator.class, (Object)new FindJunctionOperator(), (Dictionary)null);
    }
}
