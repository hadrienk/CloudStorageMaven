package com.gkatzioura.maven.cloud.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.AwsEnvVarOverrideRegionProvider;
import com.amazonaws.regions.AwsProfileRegionProvider;
import com.amazonaws.regions.AwsRegionProvider;
import com.amazonaws.regions.AwsRegionProviderChain;
import com.amazonaws.regions.AwsSystemPropertyRegionProvider;
import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.amazonaws.regions.InstanceMetadataRegionProvider;

/**
 *
 */
public class S3StorageRegionProviderChain extends AwsRegionProviderChain {

    private String providedRegion;

    /**
     * Creates a region provider chain based on the default AWS region provider chain.
     */
    public S3StorageRegionProviderChain() {
        this(null);
    }

    /**
     * Creates a region provider chain based on the default AWS region provider chain. The provided region may be null
     * if default behavior is desired.
     *
     * @param providedRegion Specific region to
     */
    public S3StorageRegionProviderChain(final String providedRegion) {
        super(new AwsRegionProvider[]{
                new AwsDefaultEnvRegionProvider(),
                new AwsEnvVarOverrideRegionProvider(),
                new AwsSystemPropertyRegionProvider(),
                new AwsProfileRegionProvider(),
                new InstanceMetadataRegionProvider()});
        this.providedRegion = providedRegion;
    }

    /**
     * {@inheritDoc}
     *
     * @return Region or null if not found
     * @throws SdkClientException Will not be thrown
     */
    @Override
    public String getRegion() throws SdkClientException {
        if (providedRegion == null) {
            // Per comments in AwsRegionProviderChain, throwing an exception is a bug.
            // Null should be returned if not found.
            try {
                return super.getRegion();
            } catch (SdkClientException e) {
                return null;
            }
        } else {
            return providedRegion;
        }
    }

    /**
     * Get the provided region. The provided region overrides any futher region resolution.
     *
     * @return Provided region
     */
    public String getProvidedRegion() {
        return providedRegion;
    }

    /**
     * Set the provided region. The provided region overrides any futher region resolution.
     *
     * @param providedRegion Provided region
     */
    public void setProvidedRegion(String providedRegion) {
        this.providedRegion = providedRegion;
    }

}